package com.matoo.board.adapter.`in`.web.dto

import com.matoo.board.application.model.PostSummary
import java.time.Instant

data class PostSummaryResponse(
    val postId: String,
    val userId: String,
    val title: String,
    val createdAt: Instant?,
    val commentCount: Long
) {
    companion object {
        fun from(summary: PostSummary) = PostSummaryResponse(
            postId = summary.postId,
            userId = summary.userId,
            title = summary.title,
            createdAt = summary.createdAt,
            commentCount = summary.commentCount
        )
    }
}
