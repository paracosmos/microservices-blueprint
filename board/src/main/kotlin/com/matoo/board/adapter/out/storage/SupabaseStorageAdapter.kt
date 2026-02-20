package com.matoo.board.adapter.out.storage

import com.matoo.board.adapter.out.client.SupabaseStorageClient
import com.matoo.board.application.port.out.file.FileStoragePort
import com.matoo.board.application.model.StoredFile
import com.matoo.board.application.model.UploadInstructions
import com.matoo.board.domain.model.StorageProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.InputStream
import java.time.Duration

@Component
@ConditionalOnProperty(prefix = "storage", name = ["provider"], havingValue = "SUPABASE", matchIfMissing = false)
class SupabaseStorageAdapter(
    private val supabaseStorageClient: SupabaseStorageClient
) : FileStoragePort {

    override fun provider(): StorageProvider = StorageProvider.SUPABASE

    override suspend fun upload(
        key: String,
        inputStream: InputStream,
        contentType: String
    ): StoredFile {
        supabaseStorageClient.putObject(key, inputStream, contentType)
        return StoredFile(key = key, url = supabaseStorageClient.publicUrl(key))
    }

    override suspend fun download(key: String): ByteArray {
        return supabaseStorageClient.getObjectBytes(key)
    }

    override suspend fun delete(key: String) {
        supabaseStorageClient.deleteObject(key)
    }

    override suspend fun exists(key: String): Boolean {
        return supabaseStorageClient.objectExists(key)
    }

    override fun publicUrl(key: String): String {
        return supabaseStorageClient.publicUrl(key)
    }

    override suspend fun signUpload(
        key: String,
        contentType: String,
        size: Long
    ): UploadInstructions {
        val expiresInSec = Duration.ofHours(2).seconds
        val signed = supabaseStorageClient.createSignedUploadUrl(
            key = key,
            upsert = false
        )
        return UploadInstructions(
            provider = StorageProvider.SUPABASE,
            method = "PUT",
            uploadUrl = signed.url,
            headers = mapOf(
                // Supabase Signed Upload는 보통 Content-Type만 맞추면 되는 편.
                // (S3 presign처럼 content-length까지 signed header로 묶는 느낌은 덜함)
                "Content-Type" to contentType
            ),
            key = key,
            url = supabaseStorageClient.publicUrl(key),
            expiresInSec = expiresInSec,
            token = signed.token,
        )
    }
}
