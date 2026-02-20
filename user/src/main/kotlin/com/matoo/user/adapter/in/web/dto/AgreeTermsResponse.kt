package com.matoo.user.adapter.`in`.web.dto

import com.matoo.user.domain.model.terms.UserTermsAgreement
import java.time.Instant

data class AgreeTermsResponse(
    val userId: String,
    val termsId: String,
    val agree: Boolean,
    val agreeAt: Instant?,
    val updatedAt: Instant?
) {
    companion object {
        fun from(model: UserTermsAgreement): AgreeTermsResponse =
            AgreeTermsResponse(
                userId = model.userId,
                termsId = model.termsId,
                agree = model.agree,
                agreeAt = model.createdAt,
                updatedAt = model.updatedAt
            )
    }
}
