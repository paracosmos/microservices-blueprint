package com.matoo.auth.adapter.out.oauth

import com.matoo.auth.application.port.out.OAuthStrategyPort
import com.matoo.auth.application.port.out.OAuthClientPort
import com.matoo.core.dto.OAuthProvider
import com.matoo.auth.domain.model.OAuthUserInfo
import org.springframework.stereotype.Component

@Component
class OAuthClientAdapter(
    private val strategies: List<OAuthStrategyPort>
) : OAuthClientPort {

    private val strategyMap: Map<OAuthProvider, OAuthStrategyPort> =
        strategies.associateBy { it.provider }

    override suspend fun getExternalUserInfo(
        code: String,
        codeVerifier: String,
        provider: OAuthProvider
    ): OAuthUserInfo {
        val strategy = strategyMap[provider]
            ?: throw IllegalArgumentException()
        return strategy.fetchUserInfo(code, codeVerifier)
    }
}
