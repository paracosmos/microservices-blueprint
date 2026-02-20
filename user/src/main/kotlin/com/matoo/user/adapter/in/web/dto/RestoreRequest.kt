package com.matoo.user.adapter.`in`.web.dto

import com.matoo.core.constant.EmailType
import jakarta.validation.Valid

data class RestoreRequest(
    @field:Valid
    val email: EmailType
)
