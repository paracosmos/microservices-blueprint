package com.matoo.board.adapter.`in`.web

import com.matoo.board.adapter.`in`.web.dto.PostCommentResponse
import com.matoo.board.adapter.`in`.web.dto.PostSummaryResponse
import com.matoo.board.application.port.`in`.BoardQueryUseCase
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/board/public")
class BoardPublicController(
    private val boardQueryUseCase: BoardQueryUseCase,
) {
    companion object {
        private const val MAX_PAGE_SIZE = 100
    }

    // 리스트: 게시글 + 댓글 수 (댓글 본문 제외). 공개 엔드포인트이므로 페이지 크기를 상한으로 제한.
    @GetMapping
    suspend fun getPosts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<List<PostSummaryResponse>> {
        val limit = size.coerceIn(1, MAX_PAGE_SIZE)
        val offset = page.coerceAtLeast(0).toLong() * limit
        val body = boardQueryUseCase.getPostList(limit, offset.toInt())
            .map(PostSummaryResponse::from)
        return ResponseEntity.ok(body)
    }

    // 상세: 게시글 + 댓글
    @GetMapping("/{postId}")
    suspend fun getPost(
        @PathVariable postId: String
    ): ResponseEntity<PostCommentResponse> {
        val result = boardQueryUseCase.getPostWithComments(postId)
        return ResponseEntity.ok(PostCommentResponse(result.post, result.comments))
    }
}
