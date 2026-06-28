package com.matoo.board.application.port.`in`

import com.matoo.board.domain.model.Post

interface PostUseCase {
    suspend fun create(userId: String, title: String, content: String): Post

    suspend fun update(postId: String, userId: String, title: String, content: String): Post
    suspend fun delete(postId: String, userId: String)
}
