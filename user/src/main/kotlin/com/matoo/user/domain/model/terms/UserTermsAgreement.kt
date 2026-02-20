package com.matoo.user.domain.model.terms

import java.time.Instant

class UserTermsAgreement(
    val userId: String,
    val termsId: String,
    val agree: Boolean,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
