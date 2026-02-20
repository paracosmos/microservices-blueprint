package com.matoo.auth.application.port.out

import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo

interface OAuthClientPort {
    suspend fun getExternalUserInfo(code: String, codeVerifier: String, provider: OAuthProvider): OAuthUserInfo
}
