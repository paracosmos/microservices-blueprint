package com.matoo.board.adapter.out.persistence.file

import com.matoo.board.domain.model.FileStatus
import com.matoo.board.domain.model.RelatedType
import com.matoo.board.domain.model.StorageProvider
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.Instant

@Entity
@Table(name = "files")
@SQLRestriction("deleted_at IS NULL")
class FileEntity(
    @Id
    @Column(name = "file_id", length = 26, nullable = false)
    val fileId: String, // ULID(26)

    @Column(name = "user_id", length = 26)
    var userId: String? = null,

    @Column(name = "related_id", length = 26)
    var relatedId: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "related_type", length = 30)
    var relatedType: RelatedType?,

    @Column(name = "storage_key", length = 512, nullable = false)
    var storageKey: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "storage", length = 20, nullable = false)
    var storage: StorageProvider = StorageProvider.S3,

    @Column(name = "file_name", length = 255)
    var fileName: String? = null,

    @Column(name = "mime_type", length = 150)
    var mimeType: String? = null,

    @Column(name = "file_size")
    var fileSize: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    var status: FileStatus = FileStatus.PENDING,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant?,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null,
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }

    fun softDelete(by: String? = null) {
        deletedAt = Instant.now()
    }
}
