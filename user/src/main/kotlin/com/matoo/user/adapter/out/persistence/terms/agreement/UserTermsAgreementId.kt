package com.matoo.user.adapter.out.persistence.terms.agreement


import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserTermsAgreementId(
    @Column(name = "user_id", length = 26, nullable = false)
    val userId: String,

    @Column(name = "terms_id", length = 26, nullable = false)
    val termsId: String
) : Serializable
