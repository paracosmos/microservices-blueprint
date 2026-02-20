package com.matoo.board.adapter.`in`.web

import com.matoo.board.application.model.FileInfo
import com.matoo.board.application.port.`in`.FileUseCase
import com.matoo.board.application.model.PresignCommand
import com.matoo.board.application.model.PresignResult
import com.matoo.core.constant.TraceConstants
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/board/files")
class BoardFileController(
    private val fileUseCase: FileUseCase
) {

    @Profile("local")
    @PostMapping
    suspend fun uploadFile(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @RequestPart("file") filePart: FilePart
    ): ResponseEntity<FileInfo> {
        val file = fileUseCase.upload(userId, filePart)
        return ResponseEntity.ok(file)
    }

    @DeleteMapping("/{fileId}")
    suspend fun uploadFile(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @PathVariable fileId: String,
    ): ResponseEntity<Unit> {
        fileUseCase.delete(userId, fileId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/presign")
    suspend fun presign(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @Valid @RequestBody
        presignRequest: PresignCommand
    ): ResponseEntity<PresignResult> {
        val body = fileUseCase.presign(userId, presignRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PostMapping("/presign/{fileId}/callback")
    suspend fun presignCallback(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @PathVariable fileId: String,
    ): ResponseEntity<FileInfo?> {
        val body = fileUseCase.presignCallback(userId, fileId)
        return ResponseEntity.ok(body)
    }

}
