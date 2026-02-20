package com.matoo.user.domain.model.notification

data class EmailMessage(
    val to: String,
    val subject: String,
    val body: String,
    val isHtml: Boolean = false,
    val from: String? = null,
    val replyTo: String? = null
)
