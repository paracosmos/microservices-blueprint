package com.matoo.board.application.port.`in`

import com.matoo.board.application.model.FileInfo
import com.matoo.board.application.model.PresignCommand
import com.matoo.board.application.model.PresignResult
import org.springframework.http.codec.multipart.FilePart


interface FileUseCase {
    suspend fun upload(
        userId: String,
        filePart: FilePart
    ): FileInfo

    suspend fun delete(
        userId: String,
        fileId: String,
        hard: Boolean? = null
    ): Boolean

    suspend fun presign(
        userId: String,
        command: PresignCommand
    ): PresignResult

    suspend fun presignCallback(
        userId: String,
        fileId: String
    ): FileInfo?
}
