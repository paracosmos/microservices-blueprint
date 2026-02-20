package com.matoo.user.adapter.out.persistence.terms.agreement

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "user_terms_agreement")
@EntityListeners(AuditingEntityListener::class)
class UserTermsAgreementEntity(
    @EmbeddedId
    var id: UserTermsAgreementId,

    @Column(name = "agree", nullable = false)
    var agree: Boolean
) {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: Instant
}
