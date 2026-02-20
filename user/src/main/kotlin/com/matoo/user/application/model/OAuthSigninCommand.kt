package com.matoo.user.application.model

import com.matoo.core.dto.OAuthProvider
import com.matoo.core.dto.user.OAuthSigninRequest

data class OAuthSigninCommand(
    val email: String,
    val oauthProvider: OAuthProvider,
    var providerUid: String,
    var providerName: String? = null,
    var providerPicture: String? = null,
)

fun OAuthSigninRequest.toCommand(): OAuthSigninCommand {
    return OAuthSigninCommand(
        email = email.value,
        oauthProvider = oauthProvider,
        providerUid = providerUid,
        providerName = providerName,
        providerPicture = providerPicture
    )
}
