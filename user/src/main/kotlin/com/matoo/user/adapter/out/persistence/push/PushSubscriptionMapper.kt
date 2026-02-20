package com.matoo.user.adapter.out.persistence.push

import com.matoo.user.domain.model.notification.PushSubscription

object PushSubscriptionMapper {
    fun toEntity(domain: PushSubscription): PushSubscriptionEntity {
        return PushSubscriptionEntity(
            pushSubscriptionId = domain.id,
            userId = domain.userId,
            endpoint = domain.endpoint,
            p256dh = domain.p256dh,
            auth = domain.auth,
            active = domain.active,
        )
    }

    fun toDomain(entity: PushSubscriptionEntity): PushSubscription =
        PushSubscription(
            id = entity.pushSubscriptionId,
            userId = entity.userId,
            endpoint = entity.endpoint,
            p256dh = entity.p256dh,
            auth = entity.auth,
            active = entity.active
        )
}
