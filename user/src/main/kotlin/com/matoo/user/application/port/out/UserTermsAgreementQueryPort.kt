package com.matoo.user.application.port.out

import com.matoo.user.domain.model.terms.UserTermsAgreement

interface UserTermsAgreementQueryPort {
    fun findAgreements(userId: String): List<UserTermsAgreement>
    fun hasAgreed(userId: String, termsId: String): Boolean
}
