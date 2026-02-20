package com.matoo.user.application.port.`in`

import com.matoo.user.application.model.AgreeTermCommand
import com.matoo.user.domain.model.terms.UserTermsAgreement

interface UserTermsUseCase {
    fun agree(userId: String, agreeTerms: List<AgreeTermCommand>)
    fun getAgreements(userId: String): List<UserTermsAgreement>
    fun hasAgreed(userId: String, termsId: String): Boolean
}
