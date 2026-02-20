package com.matoo.auth.adapter.out.oauth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Jwk(
    val kid: String,
    val n: String,
    val e: String,
    val alg: String,
    val kty: String,
    val use: String
)

data class JwkResponse(
    val keys: List<Jwk>
)

data class AuthorizationCode(
    val code: String
)

data class IdTokenRequest(
    val idToken: String
)

data class GoogleTokenResponse(
    @param:JsonProperty("access_token") val accessToken: String,
    @param:JsonProperty("id_token") val idToken: String,
    @param:JsonProperty("expires_in") val expiresIn: Int,
    @param:JsonProperty("token_type") val tokenType: String,
    val scope: String
)

data class GoogleIdTokenPayload(
    val sub: String,
    val email: String,
    val name: String?,
    val picture: String?
)

object GoogleOAuthParams {
    const val CODE = "code"
    const val CLIENT_ID = "client_id"
    const val CLIENT_SECRET = "client_secret"
    const val CODE_VERIFIER = "code_verifier"
    const val GRANT_TYPE = "grant_type"
    const val REDIRECT_URI = "redirect_uri"
}

object OAuthGrantTypes {
    const val AUTHORIZATION_CODE = "authorization_code"
    const val REFRESH_TOKEN = "refresh_token"
}
