package com.matoo.user.adapter.out.persistence.terms.agreement

import org.springframework.data.jpa.repository.JpaRepository

interface UserTermsAgreementJpaRepository : JpaRepository<UserTermsAgreementEntity, UserTermsAgreementId> {
    fun findAllByIdUserId(userId: String): List<UserTermsAgreementEntity>
    fun existsByIdUserIdAndIdTermsId(userId: String, termsId: String): Boolean
}
