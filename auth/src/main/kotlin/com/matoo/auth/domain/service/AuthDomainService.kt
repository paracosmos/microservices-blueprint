package com.matoo.auth.domain.service

import org.springframework.stereotype.Component

@Component
class AuthDomainService {
    fun validateUser(userId: String) {
        // todo if (user.isLocked())
        // todo if (user.isPasswordExpired())
        require(userId.isNotBlank())
    }
}
