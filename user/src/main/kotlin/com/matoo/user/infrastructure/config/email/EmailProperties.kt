package com.matoo.user.infrastructure.config.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "email")
data class EmailProperties(
    val smtp: Smtp,
    val ses: Ses
) {
    data class Smtp(
        val from: String,
        val enabled: Boolean = true
    )

    data class Ses(
        val from: String,
        val enabled: Boolean = true
    )
}
