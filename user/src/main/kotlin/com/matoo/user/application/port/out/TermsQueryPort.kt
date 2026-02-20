package com.matoo.user.application.port.out

import com.matoo.user.domain.model.terms.TermType
import com.matoo.user.domain.model.terms.Terms

interface TermsQueryPort {
    fun findLatestByType(type: TermType): Terms?
    fun findAllByType(type: TermType): List<Terms>
    fun findLatestByTypes(types: List<TermType>): Map<TermType, Terms>
}
