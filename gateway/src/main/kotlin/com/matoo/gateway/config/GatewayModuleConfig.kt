package com.matoo.gateway.config

import com.matoo.core.filter.ReactiveContextFilter
import com.matoo.core.support.exception.GlobalExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ReactiveContextFilter::class,
    GlobalExceptionHandler::class,
)
class GatewayModuleConfig {
}
