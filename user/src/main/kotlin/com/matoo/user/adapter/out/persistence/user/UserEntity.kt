package com.matoo.user.adapter.out.persistence.user

import com.matoo.user.adapter.out.persistence.provider.ProviderEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.time.Instant

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
open class UserEntity(
    @Id
    @Column(name = "user_id", nullable = false, length = 26)
    var userId: String,

    @Column(name = "email", nullable = false, length = 512)
    var email: String,

    @Column(name = "name", length = 512)
    var name: String? = null,

    @Column(name = "password", length = 512)
    var password: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    var providers: MutableList<ProviderEntity> = mutableListOf()
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
