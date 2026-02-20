package com.matoo.core.dto.user

import com.matoo.core.constant.EmailType
import com.matoo.core.dto.OAuthProvider
import jakarta.validation.Valid

data class OAuthSigninRequest(
    @field:Valid
    val email: EmailType,
    val oauthProvider: OAuthProvider,
    var providerUid: String,
    var providerName: String? = null,
    var providerPicture: String? = null,
)
