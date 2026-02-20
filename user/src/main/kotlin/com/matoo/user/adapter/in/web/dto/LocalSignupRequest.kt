package com.matoo.user.adapter.`in`.web.dto

import com.matoo.core.constant.EmailType
import com.matoo.core.constant.PasswordType
import jakarta.validation.Valid

data class LocalSignupRequest(
    @field:Valid
    val email: EmailType,
    @field:Valid
    val password: PasswordType,
    val name: String?
)
