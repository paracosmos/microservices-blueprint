package com.matoo.user.adapter.`in`.web.dto

data class PushSubscribeRequest(
    val endpoint: String,
    val keys: Keys
) {
    data class Keys(
        val p256dh: String,
        val auth: String
    )
}
