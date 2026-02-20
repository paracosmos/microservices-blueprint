package com.matoo.user.adapter.out.persistence.push

import org.springframework.data.jpa.repository.JpaRepository

interface PushSubscriptionJpaRepository : JpaRepository<PushSubscriptionEntity, String> {
    fun findByEndpoint(endpoint: String): PushSubscriptionEntity?
    fun findAllByUserIdAndActiveIsTrue(userId: String): List<PushSubscriptionEntity>
    fun findByUserIdAndEndpoint(userId: String, endpoint: String): PushSubscriptionEntity?
}
