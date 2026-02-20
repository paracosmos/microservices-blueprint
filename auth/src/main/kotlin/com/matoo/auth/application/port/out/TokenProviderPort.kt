package com.matoo.auth.application.port.out

import com.matoo.auth.domain.model.TokenPair

interface TokenProviderPort {
    fun generate(userId: String, claims: Map<String, Any>? = null): TokenPair
}
