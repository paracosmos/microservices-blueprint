package com.matoo.auth.infrastructure.config.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ExternalWebClientConfig(
    @param:Qualifier("externalWebClientBuilder")
    private val externalBuilder: WebClient.Builder
) {
    @Bean
    fun googleOAuthWebClient(
        @Value("\${google.oauth.uri}")
        baseUrl: String
    ): WebClient = externalBuilder
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build()
}
