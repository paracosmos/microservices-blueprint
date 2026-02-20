package com.matoo.user.domain.model.provider

import com.matoo.core.dto.OAuthProvider

enum class ProviderKey(val code: Char) {
    GOOGLE('G'),
    APPLE('A'),
    LOCAL('L');

    companion object {
        fun fromCode(code: Char): ProviderKey = entries.find { it.code == code } ?: LOCAL

        fun fromOAuthProvider(provider: OAuthProvider): ProviderKey =
            when (provider) {
                OAuthProvider.GOOGLE -> GOOGLE
                OAuthProvider.APPLE -> APPLE
            }
    }
}
