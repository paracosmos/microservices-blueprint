package com.matoo.board.adapter.out.persistence.post

import com.matoo.core.constant.IdType
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.Instant

@Entity
@Table(name = "post")
@SQLRestriction("deleted_at IS NULL")
open class PostEntity(
    @Id
    @Column(name = "post_id", nullable = false)
    val postId: IdType,

    @Column(name = "user_id", nullable = false)
    var userId: IdType,

    @Column(name = "title", length = 200, nullable = false)
    var title: String,

    // @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
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
