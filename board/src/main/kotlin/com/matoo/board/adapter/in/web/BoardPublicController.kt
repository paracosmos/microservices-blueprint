package com.matoo.board.adapter.`in`.web

import com.matoo.board.adapter.`in`.web.dto.PostCommentResponse
import com.matoo.board.application.port.`in`.BoardQueryUseCase
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/board/public")
class BoardPublicController(
    private val boardQueryUseCase: BoardQueryUseCase,
) {
    @GetMapping("/{postId}")
    suspend fun getPost(
        @PathVariable postId: String
    ): ResponseEntity<PostCommentResponse> {
        val body = boardQueryUseCase.getPostWithComments(postId)
        return ResponseEntity.ok(body)
    }
}
