package com.matoo.user.infrastructure.config.webpush

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "webpush")
data class WebPushProperties(
    val vapid: Vapid
) {
    data class Vapid(
        val publicKey: String,
        val privateKey: String,
        val subject: String
    )
}
