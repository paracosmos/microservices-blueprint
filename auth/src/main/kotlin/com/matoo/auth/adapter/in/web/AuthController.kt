package com.matoo.auth.adapter.`in`.web

import com.matoo.auth.adapter.`in`.web.dto.AuthResponse
import com.matoo.auth.adapter.`in`.web.dto.OAuthRequest
import com.matoo.auth.application.model.AuthRequestModel
import com.matoo.auth.application.model.OAuthRequestModel
import com.matoo.auth.application.port.`in`.AuthUseCase
import com.matoo.core.dto.user.LocalSigninRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/login")
    suspend fun login(
        @Valid
        @RequestBody request: LocalSigninRequest
    ): AuthResponse {
        val response = authUseCase.auth(
            AuthRequestModel(
                email = request.email.value,
                password = request.password.value
            )
        )
        return AuthResponse(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    @PostMapping("/oauth")
    suspend fun oauth(@RequestBody request: OAuthRequest): AuthResponse {
        val response = authUseCase.oauth(
            OAuthRequestModel(
                code = request.code,
                codeVerifier = request.codeVerifier,
                provider = request.provider,
            )
        )
        return AuthResponse(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }
}
