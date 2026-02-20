package com.matoo.board.application.service

import com.matoo.board.adapter.`in`.web.dto.PostCommentResponse
import com.matoo.board.application.port.`in`.BoardQueryUseCase
import com.matoo.board.application.port.`in`.CommentUseCase
import com.matoo.board.application.port.`in`.PostUseCase
import com.matoo.core.support.exception.orNotFound
import org.springframework.stereotype.Service

@Service
class BoardQueryService(
    private val postUseCase: PostUseCase,
    private val commentUseCase: CommentUseCase,
) : BoardQueryUseCase {
    override suspend fun getPostWithComments(postId: String): PostCommentResponse {
        val post = postUseCase.get(postId).orNotFound()
        val comments = commentUseCase.getByPost(postId)
        return PostCommentResponse(post, comments)
    }
}
