package com.matoo.user.adapter.out.persistence.terms.agreement

import com.matoo.user.application.model.AgreeTermCommand
import com.matoo.user.application.port.out.UserTermsAgreementCommandPort
import com.matoo.user.application.port.out.UserTermsAgreementQueryPort
import com.matoo.user.domain.model.terms.UserTermsAgreement
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserTermsAgreementPersistenceAdapter(
    private val repo: UserTermsAgreementJpaRepository
) : UserTermsAgreementCommandPort, UserTermsAgreementQueryPort {

    override fun findAgreements(userId: String): List<UserTermsAgreement> =
        repo.findAllByIdUserId(userId).map(UserTermsAgreementMapper::toDomain)

    override fun hasAgreed(userId: String, termsId: String): Boolean =
        repo.existsByIdUserIdAndIdTermsId(userId, termsId)

    @Transactional
    override fun agree(
        userId: String,
        agreeTermCommand: AgreeTermCommand
    ) {
        val id = UserTermsAgreementId(userId = userId, termsId = agreeTermCommand.id)
        repo.save(
            UserTermsAgreementEntity(id = id, agree = agreeTermCommand.agree)
        )
    }
}
