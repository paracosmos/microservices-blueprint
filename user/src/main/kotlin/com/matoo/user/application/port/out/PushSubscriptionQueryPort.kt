package com.matoo.user.application.port.out

import com.matoo.user.domain.model.notification.PushSubscription

interface PushSubscriptionQueryPort {
    suspend fun findActiveByUserId(userId: String): List<PushSubscription>
}
