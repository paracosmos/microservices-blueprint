package com.matoo.board.adapter.out.storage

import com.matoo.board.adapter.out.client.S3StorageClient
import com.matoo.board.application.port.out.file.FileStoragePort
import com.matoo.board.application.model.StoredFile
import com.matoo.board.application.model.UploadInstructions
import com.matoo.board.domain.model.StorageProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
@ConditionalOnProperty(prefix = "storage", name = ["provider"], havingValue = "S3", matchIfMissing = false)
class S3StorageAdapter(
    private val s3StorageClient: S3StorageClient
) : FileStoragePort {

    override fun provider() = StorageProvider.S3

    override suspend fun upload(key: String, inputStream: InputStream, contentType: String): StoredFile {
        s3StorageClient.putObject(key, inputStream, contentType)
        return StoredFile(key = key, url = s3StorageClient.publicUrl(key))
    }

    override suspend fun download(key: String): ByteArray {
        return s3StorageClient.getObjectBytes(key)
    }

    override suspend fun delete(key: String) {
        s3StorageClient.deleteObject(key)
    }

    override suspend fun exists(key: String): Boolean {
        return s3StorageClient.headObjectExists(key)
    }

    override fun publicUrl(key: String): String {
        return s3StorageClient.publicUrl(key)
    }

    override suspend fun signUpload(
        key: String,
        contentType: String,
        size: Long
    ): UploadInstructions {
        val presigned = s3StorageClient.presignPutObject(key, contentType, size)
        return UploadInstructions(
            provider = StorageProvider.S3,
            method = "PUT",
            uploadUrl = presigned.uploadUrl,
            headers = mapOf("Content-Type" to contentType),
            token = null,
            key = presigned.key,
            expiresInSec = presigned.expiresInSec,
            url = presigned.publicUrl
        )
    }
}
