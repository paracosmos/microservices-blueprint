package com.matoo.auth.adapter.out.oauth.apple

import com.matoo.auth.application.port.out.OAuthStrategyPort
import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class AppleOAuthStrategyAdapter(
) : OAuthStrategyPort {

    override val provider: OAuthProvider = OAuthProvider.APPLE

    override suspend fun fetchUserInfo(
        code: String,
        codeVerifier: String,
    ): OAuthUserInfo {
        throw ResponseStatusException(
            HttpStatus.NOT_IMPLEMENTED,
            "Apple OAuth sign-in is not supported yet"
        )
    }
}
