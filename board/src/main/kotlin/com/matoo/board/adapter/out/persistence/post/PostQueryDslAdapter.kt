package com.matoo.board.adapter.out.persistence.post

import com.matoo.board.adapter.out.persistence.comment.QCommentEntity
import com.matoo.board.application.model.PostSummary
import com.matoo.board.application.model.PostWithComments
import com.matoo.board.application.port.out.PostReadPort
import com.matoo.board.domain.model.Comment
import com.matoo.board.domain.model.Post
import com.matoo.core.constant.IdType
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * 읽기 전용 QueryDSL 어댑터.
 * soft-delete 제외는 @SQLRestriction 전파에 의존하지 않고 deleted_at IS NULL 술어를
 * 명시적으로 건다(특히 매핑되지 않은 ad-hoc join 의 comment 에는 @SQLRestriction 적용이 보장되지 않음).
 */
@Component
@Transactional(readOnly = true)
class PostQueryDslAdapter(
    private val queryFactory: JPAQueryFactory
) : PostReadPort {

    private val post = QPostEntity.postEntity
    private val comment = QCommentEntity.commentEntity

    /**
     * 리스트: post LEFT JOIN comment 후 GROUP BY 로 댓글 수를 한 번의 쿼리로 집계(N+1 없음).
     * 페이지네이션(limit/offset) 필수 — 공개 엔드포인트에서 전체 테이블 적재를 막는다.
     * 삭제된 댓글은 ON 절의 deleted_at IS NULL 로 제외(LEFT JOIN 유지 → 댓글 없는 글도 count 0 으로 포함).
     */
    override fun findSummaries(limit: Int, offset: Long): List<PostSummary> =
        queryFactory
            .select(
                Projections.constructor(
                    PostSummary::class.java,
                    post.postId,
                    post.title,
                    post.createdAt,
                    comment.commentId.count()
                )
            )
            .from(post)
            .leftJoin(comment).on(
                comment.postId.eq(post.postId).and(comment.deletedAt.isNull)
            )
            .where(post.deletedAt.isNull)
            .groupBy(post.postId, post.title, post.createdAt)
            .orderBy(post.createdAt.desc(), post.postId.desc())
            .offset(offset)
            .limit(limit.toLong())
            .fetch()

    /**
     * 상세: 컬렉션(댓글)이 포함되므로 cartesian product 를 피하기 위해
     * 게시글 projection 1건 + 댓글 projection 리스트, 총 2개의 projection 쿼리로 조회 후 조립한다.
     */
    override fun findDetail(postId: IdType): PostWithComments? {
        val detail = queryFactory
            .select(
                Projections.constructor(
                    Post::class.java,
                    post.postId,
                    post.userId,
                    post.title,
                    post.content,
                    post.createdAt,
                    post.updatedAt
                )
            )
            .from(post)
            .where(post.postId.eq(postId), post.deletedAt.isNull)
            .fetchOne()
            ?: return null

        val comments = queryFactory
            .select(
                Projections.constructor(
                    Comment::class.java,
                    comment.commentId,
                    comment.postId,
                    comment.userId,
                    comment.content,
                    comment.createdAt,
                    comment.updatedAt
                )
            )
            .from(comment)
            .where(comment.postId.eq(postId), comment.deletedAt.isNull)
            .orderBy(comment.createdAt.asc(), comment.commentId.asc())
            .fetch()

        return PostWithComments(detail, comments)
    }
}
