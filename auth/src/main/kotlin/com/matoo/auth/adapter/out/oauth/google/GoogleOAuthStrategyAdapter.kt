package com.matoo.auth.adapter.out.oauth.google

import com.fasterxml.jackson.databind.ObjectMapper
import com.matoo.auth.adapter.out.oauth.dto.GoogleIdTokenPayload
import com.matoo.auth.adapter.out.oauth.dto.GoogleOAuthParams
import com.matoo.auth.adapter.out.oauth.dto.GoogleTokenResponse
import com.matoo.auth.adapter.out.oauth.dto.OAuthGrantTypes
import com.matoo.auth.application.port.out.OAuthStrategyPort
import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.Base64

@Component
class GoogleOAuthStrategyAdapter(
    private val objectMapper: ObjectMapper,
    private val googleOAuthWebClient: WebClient,
    @param:Value("\${google.oauth.client-id}")
    private val clientId: String,
    @param:Value("\${google.oauth.client-secret}")
    private val clientSecret: String,
    @param:Value("\${google.oauth.redirect}")
    private val redirect: String
) : OAuthStrategyPort {


    override val provider: OAuthProvider = OAuthProvider.GOOGLE

    override suspend fun fetchUserInfo(code: String, codeVerifier: String): OAuthUserInfo {
        // Google에 Token 요청 (Code + Verifier + Secret 조합)
        val responseBody = googleOAuthWebClient.post().uri("/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(
                BodyInserters
                    .fromFormData(GoogleOAuthParams.CODE, code)
                    .with(GoogleOAuthParams.CLIENT_ID, clientId)
                    .with(GoogleOAuthParams.CLIENT_SECRET, clientSecret)
                    .with(GoogleOAuthParams.CODE_VERIFIER, codeVerifier) // FE verifier
                    .with(GoogleOAuthParams.GRANT_TYPE, OAuthGrantTypes.AUTHORIZATION_CODE)
                    .with(GoogleOAuthParams.REDIRECT_URI, redirect) // FE redirect_uri identical
            ).retrieve()
            .awaitBody<GoogleTokenResponse>()     // .awaitBody<String>()
        return decodeIdToken(responseBody.idToken)
    }

    private fun decodeIdToken(idToken: String): OAuthUserInfo {
        return runCatching {
            // JWT [header].[payload].[signature]
            val payloadBase64 = idToken.split(".").getOrNull(1) ?: throw IllegalArgumentException()
            // Base64Url(JWT)
            val decodedPayload = String(Base64.getUrlDecoder().decode(payloadBase64))
            objectMapper.readValue(decodedPayload, GoogleIdTokenPayload::class.java)
        }.map { payload ->
            OAuthUserInfo(
                id = payload.sub,
                email = payload.email,
                name = payload.name,
                picture = payload.picture,
                provider = OAuthProvider.GOOGLE
            )
        }.getOrThrow() // 실패 시 상위 에러 핸들러로 위임
    }
}
