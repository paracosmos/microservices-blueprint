package com.matoo.user.infrastructure.config

import com.matoo.core.filter.ReactiveContextFilter
import com.matoo.core.support.exception.GlobalExceptionHandler
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Hooks

@Configuration
@Import(
    ReactiveContextFilter::class,
    GlobalExceptionHandler::class,
)
class UserModuleConfig {
    @PostConstruct
    fun enableAutomaticContextPropagation() {
        Hooks.enableAutomaticContextPropagation()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()
}
