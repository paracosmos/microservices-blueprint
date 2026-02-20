package com.matoo.gateway.config

import org.springframework.boot.context.properties.*
import org.springframework.context.annotation.*

@Configuration
@ConfigurationProperties(prefix = "router")
class WhitelistConfig {
    var whitelist: List<String> = emptyList()
}
