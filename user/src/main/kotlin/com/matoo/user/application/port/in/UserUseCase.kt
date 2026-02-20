package com.matoo.user.application.port.`in`

import com.matoo.core.constant.IdType
import com.matoo.user.application.model.LocalSigninCommand
import com.matoo.user.application.model.SigninQuery
import com.matoo.user.application.model.LocalSignupCommand
import com.matoo.user.application.model.OAuthSigninCommand

interface UserUseCase {
    suspend fun signupLocal(command: LocalSignupCommand): Boolean
    suspend fun signinLocal(command: LocalSigninCommand): SigninQuery
    suspend fun signinOAuth(command: OAuthSigninCommand): SigninQuery

    suspend fun signoff(userId: IdType)
    suspend fun restore(email: String)
}
