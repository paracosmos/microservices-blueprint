package com.matoo.board.application.port.out

import com.matoo.board.application.model.PostSummary
import com.matoo.board.application.model.PostWithComments
import com.matoo.core.constant.IdType

/**
 * 읽기 전용 조회 포트.
 * - 리스트: 댓글 수 집계가 필요해 QueryDSL 집계 쿼리로 구현.
 * - 상세: 게시글 + 댓글을 projection 으로 조회.
 */
interface PostReadPort {
    fun findSummaries(limit: Int, offset: Long): List<PostSummary>
    fun findDetail(postId: IdType): PostWithComments?
}
