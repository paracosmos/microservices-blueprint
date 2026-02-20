package com.matoo.board.application.port.`in`

import com.matoo.board.adapter.`in`.web.dto.PostCommentResponse

interface BoardQueryUseCase {
    suspend fun getPostWithComments(postId: String): PostCommentResponse
}
