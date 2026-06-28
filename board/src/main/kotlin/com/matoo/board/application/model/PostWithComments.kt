package com.matoo.board.application.model

import com.matoo.board.domain.model.Comment
import com.matoo.board.domain.model.Post

data class PostWithComments(
    val post: Post,
    val comments: List<Comment>
)
