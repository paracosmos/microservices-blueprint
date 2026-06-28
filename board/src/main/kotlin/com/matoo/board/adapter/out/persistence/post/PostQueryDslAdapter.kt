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
 * 엔티티의 @SQLRestriction("deleted_at IS NULL") 가 JPQL 에도 적용되어
 * soft-delete 된 게시글/댓글은 자동 제외된다(댓글 수 집계 포함).
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
     */
    override fun findSummaries(): List<PostSummary> =
        queryFactory
            .select(
                Projections.constructor(
                    PostSummary::class.java,
                    post.postId,
                    post.userId,
                    post.title,
                    post.createdAt,
                    comment.commentId.count()
                )
            )
            .from(post)
            .leftJoin(comment).on(comment.postId.eq(post.postId))
            .groupBy(post.postId, post.userId, post.title, post.createdAt)
            .orderBy(post.createdAt.desc())
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
            .where(post.postId.eq(postId))
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
            .where(comment.postId.eq(postId))
            .orderBy(comment.createdAt.asc())
            .fetch()

        return PostWithComments(detail, comments)
    }
}
