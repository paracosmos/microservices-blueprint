package com.matoo.board.adapter.`in`.web

import com.matoo.board.adapter.`in`.web.dto.PostCreateRequest
import com.matoo.board.adapter.`in`.web.dto.CommentCreateRequest
import com.matoo.board.application.port.`in`.CommentUseCase
import com.matoo.board.application.port.`in`.PostUseCase
import com.matoo.board.domain.model.Post
import com.matoo.core.constant.TraceConstants
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/board")
class BoardController(
    private val postUseCase: PostUseCase,
    private val commentUseCase: CommentUseCase,
) {
    @PostMapping("/posts")
    suspend fun createPost(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String, @Valid @RequestBody req: PostCreateRequest
    ): ResponseEntity<Post> {
        val body = postUseCase.create(userId, req.title, req.content)
        return ResponseEntity.ok(body)
    }

    @PutMapping("/posts/{postId}")
    suspend fun updatePost(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @PathVariable postId: String,
        @Valid @RequestBody req: PostCreateRequest
    ): ResponseEntity<Post> {
        val body = postUseCase.update(postId, userId, req.title, req.content)
        return ResponseEntity.ok(body)
    }

    @DeleteMapping("/posts/{postId}")
    suspend fun deletePost(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String, @PathVariable postId: String
    ): ResponseEntity<Unit> {
        postUseCase.delete(postId, userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/posts/{postId}/comments")
    suspend fun createComment(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @PathVariable postId: String,
        @Valid @RequestBody req: CommentCreateRequest
    ): ResponseEntity<Unit> {
        commentUseCase.addComment(postId, userId, req.content)
        return ResponseEntity.ok().build()
    }

}
