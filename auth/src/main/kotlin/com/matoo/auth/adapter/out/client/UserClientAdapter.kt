package com.matoo.auth.adapter.out.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.matoo.auth.application.port.out.UserClientPort
import com.matoo.core.dto.user.LocalSigninRequest
import com.matoo.core.dto.user.SigninResponse
import com.matoo.core.dto.user.OAuthSigninRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class UserClientAdapter(
    private val objectMapper: ObjectMapper,
    private val userClient: WebClient
) : UserClientPort {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun signinLocal(body: LocalSigninRequest): SigninResponse {
        return userClient.post()
            .uri("/internal/signin/local")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(SigninResponse::class.java)
//            .transformDeferred(RetryOperator.of(defaultRetry))
            .awaitSingle()
    }

    override suspend fun signinOAuth(body: OAuthSigninRequest): SigninResponse {
        return userClient.post()
            .uri("/internal/signin/oauth")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(SigninResponse::class.java)
            .doOnSubscribe { println("Request: ${objectMapper.writeValueAsString(body)}") }
            .awaitSingle()
    }
}
