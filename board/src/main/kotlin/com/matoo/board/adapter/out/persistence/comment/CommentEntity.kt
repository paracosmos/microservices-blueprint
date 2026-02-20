package com.matoo.board.adapter.out.persistence.comment

import com.matoo.core.constant.IdType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.time.Instant

@Entity
@Table(name = "comment")
@SQLRestriction("deleted_at IS NULL")
class CommentEntity(
    @Id
    @Column(name = "comment_id", nullable = false)
    val commentId: IdType,

    @Column(name = "post_id", nullable = false)
    var postId: IdType,

    @Column(name = "user_id", nullable = false)
    var userId: IdType,

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant?,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
