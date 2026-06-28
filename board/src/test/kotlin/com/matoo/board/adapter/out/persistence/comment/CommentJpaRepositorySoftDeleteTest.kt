package com.matoo.board.adapter.out.persistence.comment

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * 회귀 테스트: CommentJpaRepository.softDeleteById 가 commentId(PK)로 필터해야 한다.
 *
 * 과거 버그에서는 쿼리가 `where c.postId = :id` 로 되어 있어 commentId 를 넘겨도
 * 어떤 행과도 매칭되지 않아 댓글 삭제가 동작하지 않았다(영향 행 0). 이 테스트는
 * 그 회귀를 막는다.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
    properties = [
        "spring.flyway.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:board_soft_delete;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    ]
)
class CommentJpaRepositorySoftDeleteTest {

    @Autowired
    lateinit var commentRepository: CommentJpaRepository

    private fun newComment(commentId: String, postId: String) = CommentEntity(
        commentId = commentId,
        postId = postId,
        userId = "user-1",
        content = "content",
        createdAt = null,
    )

    @Test
    fun `softDeleteById removes exactly the target comment and leaves siblings on the same post`() {
        val postId = "post-1"
        val target = commentRepository.save(newComment("comment-1", postId))
        val sibling = commentRepository.save(newComment("comment-2", postId))

        val affected = commentRepository.softDeleteById(target.commentId)

        // 정확히 대상 1행만 갱신되어야 한다(과거 버그에서는 0행).
        assertEquals(1, affected)
        // @SQLRestriction("deleted_at IS NULL") 로 soft-delete 된 댓글은 조회되지 않는다.
        assertNull(commentRepository.findByIdOrNull(target.commentId), "target comment should be soft-deleted")
        // 같은 게시글의 다른 댓글은 영향을 받지 않아야 한다.
        assertNotNull(commentRepository.findByIdOrNull(sibling.commentId), "sibling comment must remain")
    }

    @Test
    fun `softDeleteById does not affect comments of other posts`() {
        val target = commentRepository.save(newComment("comment-a", "post-A"))
        val other = commentRepository.save(newComment("comment-b", "post-B"))

        commentRepository.softDeleteById(target.commentId)

        assertNull(commentRepository.findByIdOrNull(target.commentId))
        assertNotNull(commentRepository.findByIdOrNull(other.commentId))
    }

    @Test
    fun `softDeleteById is idempotent - second call affects no rows`() {
        val target = commentRepository.save(newComment("comment-x", "post-X"))

        assertEquals(1, commentRepository.softDeleteById(target.commentId))
        assertEquals(0, commentRepository.softDeleteById(target.commentId))
    }
}
