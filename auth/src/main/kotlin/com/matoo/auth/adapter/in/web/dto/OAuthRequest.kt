package com.matoo.auth.adapter.`in`.web.dto

import com.matoo.core.dto.OAuthProvider

data class OAuthRequest(
    val code: String,
    val codeVerifier: String,
    val provider: OAuthProvider
)
