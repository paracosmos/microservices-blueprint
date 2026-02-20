package com.matoo.user.application.model

data class LocalSigninCommand(
    val email: String,
    val password: String,
)
