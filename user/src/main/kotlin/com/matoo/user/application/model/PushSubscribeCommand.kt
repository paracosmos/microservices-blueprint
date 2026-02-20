package com.matoo.user.application.model

data class PushSubscribeCommand(
    val endpoint: String,
    val p256dh: String,
    val auth: String
)
