package com.matoo.board.application.service.file

import com.matoo.board.application.model.FileInfo
import com.matoo.board.application.port.`in`.FileUseCase
import com.matoo.board.application.model.PresignCommand
import com.matoo.board.application.model.PresignResult
import com.matoo.board.application.model.toFileInfo
import com.matoo.board.application.port.out.file.FileCommandPort
import com.matoo.board.application.port.out.file.FileQueryPort
import com.matoo.board.application.port.out.file.FileStoragePort
import com.matoo.board.domain.model.File
import com.matoo.board.domain.model.FileStatus
import com.matoo.core.support.exception.requireStatus
import com.matoo.core.util.CoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class FileService(
    private val storagePort: FileStoragePort,
    private val fileCommandPort: FileCommandPort,
    private val fileQueryPort: FileQueryPort
) : FileUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val storage = storagePort.provider()

    companion object {
        private const val MAX_BYTES: Long = 10L * 1024 * 1024 // 10mb
    }

    override suspend fun upload(
        userId: String, filePart: FilePart
    ): FileInfo {
        val fileId = CoreUtil.generateId()
        val key = generateStorageKey(fileId, filePart.filename())
        val bytes = readAllBytesWithLimit(filePart, MAX_BYTES)
        val fileName = filePart.filename()
        val mimeType =
            filePart.headers().contentType?.toString()?.takeIf { it.isNotBlank() }
                ?: MediaTypeFactory.getMediaType(fileName).map { it.toString() }
                    .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE)
        val stored = storagePort.upload(
            key = key,
            inputStream = bytes.inputStream(),
            contentType = mimeType
        )
        withContext(Dispatchers.IO) {
            fileCommandPort.save(
                File(
                    fileId = fileId,
                    userId = userId,
                    storageKey = stored.key,
                    storage = storage,
                    fileName = fileName,
                    mimeType = mimeType,
                    fileSize = bytes.size.toLong(),
                    status = FileStatus.ACTIVE
                )
            )
        }
        return FileInfo(
            fileId = fileId,
            userId = userId,
            storageKey = stored.key,
            url = stored.url,
            fileName = filePart.filename(),
            mimeType = filePart.headers().contentType?.toString(),
            fileSize = bytes.size.toLong()
        )
    }


    private suspend fun readAllBytesWithLimit(filePart: FilePart, maxBytes: Long): ByteArray {
        val dataBuffer = DataBufferUtils.join(filePart.content()).awaitSingle()
        return try {
            val size = dataBuffer.readableByteCount().toLong()
            requireStatus(
                condition = size <= maxBytes,
                status = HttpStatus.PAYLOAD_TOO_LARGE
            )
            ByteArray(size.toInt()).also { dataBuffer.read(it) }
        } finally {
            DataBufferUtils.release(dataBuffer)
        }
    }

    override suspend fun delete(
        userId: String, fileId: String, hard: Boolean?
    ): Boolean {
        val file = withContext(Dispatchers.IO) { fileQueryPort.findById(fileId) } ?: return false
        // DB 행을 먼저 soft-delete 해 깨진 링크 노출을 막는다.
        withContext(Dispatchers.IO) { fileCommandPort.deleteById(fileId) }
        // 스토리지 삭제는 best-effort: 실패해도 요청을 실패시키지 않고(이미 행은 삭제됨, 재시도 시 false 만 반환)
        // 고아 객체로 로그만 남겨 별도 정리(GC) 대상으로 둔다.
        runCatching { storagePort.delete(file.storageKey) }
            .onFailure { logger.warn("orphaned storage object after delete: key={}", file.storageKey, it) }
        return true
    }

    override suspend fun presign(
        userId: String, command: PresignCommand
    ): PresignResult {
        val fileId = CoreUtil.generateId()
        val key = generateStorageKey(fileId, command.fileName)
        val presigned = storagePort.signUpload(
            key = key, contentType = command.contentType, size = command.size
        )
        withContext(Dispatchers.IO) {
            fileCommandPort.save(
                File(
                    fileId = fileId,
                    userId = userId,
                    storageKey = presigned.key,
                    storage = storage,
                    fileName = command.fileName,
                    mimeType = command.contentType,
                    fileSize = command.size,
                )
            )
        }
        return PresignResult(
            uploadUrl = presigned.uploadUrl, fileId = fileId, expiresInSec = presigned.expiresInSec, url = presigned.url
        )
    }

    override suspend fun presignCallback(
        userId: String, fileId: String
    ): FileInfo? {
        val file = withContext(Dispatchers.IO) { fileQueryPort.findById(fileId) } ?: return null
        if (file.status == FileStatus.ACTIVE) return file.toFileInfo()

        if (file.userId != null && file.userId != userId) return null
        if (!storagePort.exists(file.storageKey)) {
            return null
        }

        withContext(Dispatchers.IO) { fileCommandPort.markUploaded(fileId, userId) }
        return file.toFileInfo()
    }

    private fun generateStorageKey(fileId: String, fileName: String): String = "files/$fileId/$fileName"
}
