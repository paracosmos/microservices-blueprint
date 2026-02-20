package com.matoo.user.adapter.out.persistence.terms

import com.matoo.user.domain.model.terms.TermType
import org.springframework.data.jpa.repository.JpaRepository

interface TermsJpaRepository : JpaRepository<TermsEntity, String> {
    fun findAllByTypeOrderByCreatedAtDesc(type: TermType): List<TermsEntity>
    fun findFirstByTypeOrderByCreatedAtDesc(type: TermType): TermsEntity?
    fun findAllByTypeInOrderByTypeAscCreatedAtDesc(types: Collection<TermType>): List<TermsEntity>
}
