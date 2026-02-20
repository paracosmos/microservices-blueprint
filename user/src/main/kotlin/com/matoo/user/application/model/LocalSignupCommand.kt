package com.matoo.user.application.model

data class LocalSignupCommand(
    val email: String,
    val password: String,
    val name: String?
)
