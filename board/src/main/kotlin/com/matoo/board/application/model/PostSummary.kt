package com.matoo.board.application.model

import com.matoo.core.constant.IdType
import java.time.Instant

/**
 * 게시글 리스트 항목. 댓글 본문은 포함하지 않고 댓글 수만 노출한다.
 * 공개(비인증) 목록에 노출되므로 작성자 내부 식별자(userId)는 포함하지 않는다.
 */
data class PostSummary(
    val postId: IdType,
    val title: String,
    val createdAt: Instant?,
    val commentCount: Long
)
