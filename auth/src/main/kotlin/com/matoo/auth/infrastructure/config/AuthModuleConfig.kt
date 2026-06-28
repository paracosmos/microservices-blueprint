package com.matoo.auth.infrastructure.config

import com.matoo.auth.domain.service.AuthDomainService
import com.matoo.core.filter.ReactiveContextFilter
import com.matoo.core.support.client.WebClientProfileConfig
import com.matoo.core.support.exception.GlobalExceptionHandler
import com.matoo.core.support.client.WebClientRootConfig
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import reactor.core.publisher.Hooks

@Configuration
@Import(
    ReactiveContextFilter::class,
    WebClientRootConfig::class,
    WebClientProfileConfig::class,
    GlobalExceptionHandler::class,
)
class AuthModuleConfig() {
    @PostConstruct
    fun enableAutomaticContextPropagation() {
        Hooks.enableAutomaticContextPropagation()
    }

    @Bean
    fun authDomainService(): AuthDomainService = AuthDomainService()
}
