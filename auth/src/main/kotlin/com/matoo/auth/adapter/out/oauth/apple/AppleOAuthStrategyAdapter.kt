package com.matoo.auth.adapter.out.oauth.apple

import com.matoo.auth.application.port.out.OAuthStrategyPort
import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo
import org.springframework.stereotype.Component

@Component
class AppleOAuthStrategyAdapter(
) : OAuthStrategyPort {

    override val provider: OAuthProvider = OAuthProvider.APPLE

    override suspend fun fetchUserInfo(
        code: String,
        codeVerifier: String,
    ): OAuthUserInfo {
        TODO("Not yet implemented")
    }
}
