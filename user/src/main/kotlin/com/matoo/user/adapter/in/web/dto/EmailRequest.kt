package com.matoo.user.adapter.`in`.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class EmailRequest(
    @field:Email
    val to: String,
    @field:NotBlank
    val subject: String,
    @field:NotBlank
    val body: String,
    val isHtml: Boolean = false
)
