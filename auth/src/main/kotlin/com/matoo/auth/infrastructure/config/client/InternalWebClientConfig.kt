package com.matoo.auth.infrastructure.config.client

import com.matoo.core.constant.RemoteService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class InternalWebClientConfig(
    private val internalBuilder: WebClient.Builder
) {
    @Bean
    fun userClient(): WebClient {
        val userBaseUrl = RemoteService.USER.baseUrl + "/api/v1/user"
        return internalBuilder
            .baseUrl(userBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}
