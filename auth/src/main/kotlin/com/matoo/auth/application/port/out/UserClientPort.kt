package com.matoo.auth.application.port.out

import com.matoo.core.dto.user.LocalSigninRequest
import com.matoo.core.dto.user.OAuthSigninRequest
import com.matoo.core.dto.user.SigninResponse

interface UserClientPort {
    suspend fun signinLocal(body: LocalSigninRequest): SigninResponse
    suspend fun signinOAuth(body: OAuthSigninRequest): SigninResponse
}
