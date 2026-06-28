package com.matoo.board.application.port.`in`

import com.matoo.board.domain.model.Comment

interface CommentUseCase {
    suspend fun addComment(postId: String, userId: String, content: String): Comment
    suspend fun getByPost(postId: String): List<Comment>
}
