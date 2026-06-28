package com.matoo.auth.domain.service

class AuthDomainService {
    fun validateUser(userId: String) {
        // todo if (user.isLocked())
        // todo if (user.isPasswordExpired())
        require(userId.isNotBlank())
    }
}
