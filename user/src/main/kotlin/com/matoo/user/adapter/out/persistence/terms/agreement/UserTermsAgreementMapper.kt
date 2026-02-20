package com.matoo.user.adapter.out.persistence.terms.agreement

import com.matoo.user.domain.model.terms.UserTermsAgreement

object UserTermsAgreementMapper {
    fun toDomain(e: UserTermsAgreementEntity): UserTermsAgreement = UserTermsAgreement(
        userId = e.id.userId,
        termsId = e.id.termsId,
        agree = e.agree,
        createdAt = e.createdAt,
        updatedAt = e.updatedAt
    )
}
