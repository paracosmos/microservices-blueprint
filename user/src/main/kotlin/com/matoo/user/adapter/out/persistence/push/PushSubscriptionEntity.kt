package com.matoo.user.adapter.out.persistence.push

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.PreUpdate
import java.time.Instant

@Entity
@Table(
    name = "push_subscription",
//    indexes = [
//        Index(name = "idx_push_sub_user_active", columnList = "user_id, active")
//    ],
//    uniqueConstraints = [
//        UniqueConstraint(name = "uk_push_sub_endpoint", columnNames = ["endpoint"])
//    ]
)
class PushSubscriptionEntity(
    @Id
    @Column(name = "push_subscription_id", length = 26)
    var pushSubscriptionId: String,

    @Column(name = "user_id", nullable = false, length = 26)
    var userId: String,

    @Column(name = "endpoint", nullable = false, length = 500)
    var endpoint: String,

    @Column(name = "p256dh", nullable = false, length = 255)
    var p256dh: String,

    @Column(name = "auth", nullable = false, length = 255)
    var auth: String,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }

    fun deactivate() {
        this.active = false
    }

    fun touch(p256dh: String, auth: String) {
        this.p256dh = p256dh
        this.auth = auth
    }
}
