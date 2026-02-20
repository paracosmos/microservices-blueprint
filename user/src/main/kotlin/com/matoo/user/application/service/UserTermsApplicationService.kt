package com.matoo.user.application.service

import com.matoo.user.application.model.AgreeTermCommand
import com.matoo.user.application.port.`in`.UserTermsUseCase
import com.matoo.user.application.port.out.UserTermsAgreementCommandPort
import com.matoo.user.application.port.out.UserTermsAgreementQueryPort
import com.matoo.user.domain.model.terms.UserTermsAgreement
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserTermsApplicationService(
    private val agreementCommandPort: UserTermsAgreementCommandPort,
    private val agreementQueryPort: UserTermsAgreementQueryPort
) : UserTermsUseCase {

    @Transactional
    override fun agree(
        userId: String,
        agreeTerms: List<AgreeTermCommand>
    ) {
        agreeTerms.distinctBy { it.id }.forEach { agreeTermCommand ->
            agreementCommandPort.agree(userId, agreeTermCommand)
        }
    }

    @Transactional(readOnly = true)
    override fun getAgreements(userId: String): List<UserTermsAgreement> =
        agreementQueryPort.findAgreements(userId)

    @Transactional(readOnly = true)
    override fun hasAgreed(userId: String, termsId: String): Boolean =
        agreementQueryPort.hasAgreed(userId, termsId)
}
