package com.matoo.auth.application.model

import com.matoo.core.dto.OAuthProvider

data class OAuthRequestModel(
    val code: String,
    val codeVerifier: String,
    val provider: OAuthProvider
)
