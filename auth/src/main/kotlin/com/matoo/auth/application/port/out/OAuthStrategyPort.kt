package com.matoo.auth.application.port.out

import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo

interface OAuthStrategyPort {
    val provider: OAuthProvider
    suspend fun fetchUserInfo(code: String, codeVerifier: String): OAuthUserInfo
}
