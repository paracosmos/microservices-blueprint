package com.matoo.user.infrastructure.config.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.ses")
data class SesProperties(
    val region: String = "ap-northeast-2",
    val accessKey: String,
    val secretKey: String
)
