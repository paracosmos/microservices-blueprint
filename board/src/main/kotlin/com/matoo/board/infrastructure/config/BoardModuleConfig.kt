package com.matoo.board.infrastructure.config

import com.matoo.core.filter.ReactiveContextFilter
import com.matoo.core.support.client.WebClientProfileConfig
import com.matoo.core.support.client.WebClientRootConfig
import com.matoo.core.support.exception.GlobalExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ReactiveContextFilter::class,
    WebClientRootConfig::class,
    WebClientProfileConfig::class,
    GlobalExceptionHandler::class,
)
class BoardModuleConfig
