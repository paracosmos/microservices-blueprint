package com.matoo.user.infrastructure.config.webpush

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(WebPushProperties::class)
class WebPushConfig
