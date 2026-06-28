package com.matoo.board.adapter.`in`.web.dto

import com.matoo.board.application.model.PostWithComments
import java.time.Instant

/**
 * 공개(비인증) 상세 응답. [PostSummaryResponse] 와 동일하게 작성자 내부 식별자(userId)는 노출하지 않는다.
 */
data class PostCommentResponse(
    val post: PostView,
    val comments: List<CommentView>
) {
    data class PostView(
        val postId: String,
        val title: String,
        val content: String,
        val createdAt: Instant?,
        val updatedAt: Instant?,
    )

    data class CommentView(
        val commentId: String,
        val content: String,
        val createdAt: Instant?,
    )

    companion object {
        fun from(detail: PostWithComments) = PostCommentResponse(
            post = PostView(
                postId = detail.post.postId,
                title = detail.post.title,
                content = detail.post.content,
                createdAt = detail.post.createdAt,
                updatedAt = detail.post.updatedAt,
            ),
            comments = detail.comments.map {
                CommentView(
                    commentId = it.commentId,
                    content = it.content,
                    createdAt = it.createdAt,
                )
            }
        )
    }
}
