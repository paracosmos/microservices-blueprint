package com.matoo.board.adapter.`in`.web.dto

import com.matoo.board.domain.model.Comment
import com.matoo.board.domain.model.Post

data class PostCommentResponse(
    val post: Post,
    val comments: List<Comment>
)
