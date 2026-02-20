package com.matoo.auth.domain.model

import com.matoo.core.dto.OAuthProvider

data class OAuthUserInfo(
    val id: String,
    val email: String,
    val provider: OAuthProvider,
    val name: String? = null,
    val picture: String? = null,
    var idToken: String? = null,
)
