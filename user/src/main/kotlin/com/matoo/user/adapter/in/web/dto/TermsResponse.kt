package com.matoo.user.adapter.`in`.web.dto

import com.matoo.user.domain.model.terms.TermType
import com.matoo.user.domain.model.terms.Terms
import java.time.Instant

data class TermsResponse(
    val termsId: String,
    val type: TermType,
    val title: String,
    val content: String,
    val contentUrl: String?,
    val required: Boolean,
    val createdAt: Instant?
)

fun Terms.toResponse(): TermsResponse =
    TermsResponse(
        termsId = termsId,
        type = type,
        title = title,
        content = content,
        contentUrl = contentUrl,
        required = required,
        createdAt = createdAt
    )
