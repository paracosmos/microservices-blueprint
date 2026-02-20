package com.matoo.auth.application.model

data class AuthResponseModel(
    val accessToken: String,
    val refreshToken: String
)
