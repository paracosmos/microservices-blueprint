package com.matoo.user.application.port.`in`

import com.matoo.user.domain.model.terms.TermType
import com.matoo.user.domain.model.terms.Terms

interface TermsUseCase {
    fun getLatestTerms(types: List<TermType>): Map<TermType, Terms>
    fun getLatestTerms(type: TermType): Terms?
    fun getTermsHistory(type: TermType): List<Terms>
}
