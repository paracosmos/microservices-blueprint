package com.matoo.user.adapter.out.persistence.provider

import com.matoo.user.adapter.out.persistence.user.UserEntity
import com.matoo.user.domain.model.provider.ProviderKey
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "provider",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "provider_key"])
    ]
)
open class ProviderEntity(
    @Id
    @Column(name = "provider_id", nullable = false, length = 26)
    var providerId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @Convert(converter = ProviderKeyConverter::class)
    @Column(name = "provider_key", nullable = false, length = 1)
    var providerKey: ProviderKey,

    @Column(name = "provider_uid", nullable = false, length = 200)
    var providerUid: String,

    @Column(name = "provider_name", length = 512)
    var providerName: String? = null,

    @Column(name = "provider_picture", columnDefinition = "TEXT")
    var providerPicture: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }
}
