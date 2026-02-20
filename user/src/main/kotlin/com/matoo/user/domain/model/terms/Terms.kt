package com.matoo.user.domain.model.terms

import java.time.Instant

data class Terms(
    val termsId: String,
    val type: TermType,
    val title: String,
    val content: String,
    val contentUrl: String?,
    val required: Boolean,
    val createdAt: Instant?,
)
