package com.matoo.user.application.service

import com.matoo.user.application.port.`in`.TermsUseCase
import com.matoo.user.application.port.out.TermsQueryPort
import com.matoo.user.domain.model.terms.TermType
import com.matoo.user.domain.model.terms.Terms
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TermsApplicationService(
    private val termsQueryPort: TermsQueryPort
) : TermsUseCase {

    @Transactional(readOnly = true)
    override fun getLatestTerms(types: List<TermType>): Map<TermType, Terms> =
        termsQueryPort.findLatestByTypes(types)

    @Transactional(readOnly = true)
    override fun getLatestTerms(type: TermType): Terms? =
        termsQueryPort.findLatestByType(type)

    @Transactional(readOnly = true)
    override fun getTermsHistory(type: TermType): List<Terms> =
        termsQueryPort.findAllByType(type)
}
