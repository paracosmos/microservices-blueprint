package com.matoo.board.application.port.`in`

import com.matoo.board.application.model.PostSummary
import com.matoo.board.application.model.PostWithComments

interface BoardQueryUseCase {
    suspend fun getPostList(): List<PostSummary>
    suspend fun getPostWithComments(postId: String): PostWithComments
}
