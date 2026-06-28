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
        userId: String, fileId: String
    ): Boolean {
        val file = withContext(Dispatchers.IO) { fileQueryPort.findById(fileId) } ?: return false
        // 소유자만 삭제 가능(IDOR 방지). 소유자 미지정(null) 파일은 허용.
        requireStatus(file.userId == null || file.userId == userId, HttpStatus.FORBIDDEN)
        // 스토리지를 먼저 삭제(S3 delete 는 멱등) → DB soft-delete 순서.
        // 성공 응답은 실제 바이트 삭제를 보장한다(개인정보/보존 관점).
        // 스토리지 삭제 실패 시 예외로 요청이 실패하고 아무것도 커밋되지 않아 재시도 안전.
        // DB 삭제가 그 뒤 실패하면 행이 남아 재시도 가능하며, 재시도 시 스토리지 삭제는 멱등 no-op.
        storagePort.delete(file.storageKey)
        withContext(Dispatchers.IO) { fileCommandPort.deleteById(fileId) }
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
