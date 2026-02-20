package com.matoo.board.application.port.out.file

import com.matoo.board.application.model.StoredFile
import com.matoo.board.application.model.UploadInstructions
import com.matoo.board.domain.model.StorageProvider
import java.io.InputStream

interface FileStoragePort {
    fun provider(): StorageProvider

    suspend fun upload(
        key: String,
        inputStream: InputStream,
        contentType: String
    ): StoredFile

    suspend fun download(key: String): ByteArray
    suspend fun delete(key: String)
    suspend fun exists(key: String): Boolean
    fun publicUrl(key: String): String

    suspend fun signUpload(
        key: String,
        contentType: String,
        size: Long
    ): UploadInstructions
}
