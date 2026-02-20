package com.matoo.user.adapter.`in`.web.dto

import com.matoo.core.constant.EmailType
import jakarta.validation.Valid

data class GoogleSignupRequest(
    @field:Valid
    val email: EmailType,
    val googleUid: String,
    val name: String?
)
