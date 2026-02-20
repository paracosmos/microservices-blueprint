package com.matoo.board.domain.model

import com.matoo.core.constant.IdType
import java.time.Instant

data class Post(
    val postId: IdType,
    val userId: IdType,
    val title: String,
    val content: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
