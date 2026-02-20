package com.matoo.board.domain.model

import com.matoo.core.constant.IdType
import java.time.Instant

data class Comment(
    val commentId: IdType,
    val postId: IdType,
    val userId: IdType,
    val content: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
