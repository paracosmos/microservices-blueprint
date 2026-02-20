package com.matoo.user.application.model

data class GoogleSignupCommand(
    val email: String,
    val googleUid: String,
    val name: String?
)
