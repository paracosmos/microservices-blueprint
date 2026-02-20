package com.matoo.user.application.port.out

import com.matoo.user.application.model.AgreeTermCommand

interface UserTermsAgreementCommandPort {
    /**
     * 멱등(idempotent)하게 동의 처리: 이미 동의되어 있으면 true 반환
     */
    fun agree(userId: String, agreeTermCommand: AgreeTermCommand)
}
