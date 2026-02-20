package com.matoo.core.dto.user

import com.matoo.core.constant.EmailType
import com.matoo.core.constant.PasswordType
import jakarta.validation.Valid

data class LocalSigninRequest(
    @field:Valid
    val email: EmailType,

    @field:Valid
    val password: PasswordType,
)
