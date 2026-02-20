package com.matoo.board.application.port.`in`

import com.matoo.board.domain.model.Post

interface PostUseCase {
    fun create(userId: String, title: String, content: String): Post

    fun get(postId: String): Post
    fun getAll(): List<Post>

    fun update(postId: String, userId: String, title: String, content: String): Post
    fun delete(postId: String, userId: String)
}
