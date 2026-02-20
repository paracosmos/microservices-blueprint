package com.matoo.auth.application.port.`in`

import com.matoo.auth.application.model.AuthRequestModel
import com.matoo.auth.application.model.AuthResponseModel
import com.matoo.auth.application.model.OAuthRequestModel

interface AuthUseCase {
    suspend fun auth(authRequestModel: AuthRequestModel): AuthResponseModel
    suspend fun oauth(oauthRequestModel: OAuthRequestModel): AuthResponseModel
}
