package com.matoo.user.domain.model.notification

data class PushSubscription(
    val id: String,
    val userId: String,
    val endpoint: String,
    val p256dh: String,
    val auth: String,
    val active: Boolean
)
