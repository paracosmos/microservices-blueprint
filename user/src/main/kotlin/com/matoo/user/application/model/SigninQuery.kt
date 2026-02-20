package com.matoo.user.application.model

import com.matoo.core.dto.user.SigninResponse

data class SigninQuery(
    val userId: String, val email: String
)

fun SigninQuery.toResponse(): SigninResponse {
    return SigninResponse(
        userId = userId, email = email
    )
}
