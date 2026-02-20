package com.matoo.auth.adapter.out.jwt

import com.matoo.auth.application.port.out.TokenProviderPort
import com.matoo.auth.domain.model.TokenPair
import com.matoo.core.constant.TraceConstants
import com.matoo.core.util.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenProviderAdapter(
    @param:Value("\${jwt.access.private-key}") private val accessPrivateKey: String,
    @param:Value("\${jwt.refresh.private-key}") private val refreshPrivateKey: String
) : TokenProviderPort {

    override fun generate(userId: String, claims: Map<String, Any>?): TokenPair {
        val accessToken =
            JwtUtil.generateToken(
                subject = userId,
                privateKeyString = accessPrivateKey,
                claims = claims,
                TraceConstants.ACCESS_TOKEN_TTL
            )
        val refreshToken =
            JwtUtil.generateToken(
                subject = userId,
                privateKeyString = refreshPrivateKey,
                claims = claims,
                TraceConstants.REFRESH_TOKEN_TTL
            )
        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
