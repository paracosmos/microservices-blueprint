package com.matoo.user.application.port.out

import com.matoo.user.domain.model.user.User
import com.matoo.core.port.QueryPort

interface UserQueryPort : QueryPort<User> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
    fun restoreByEmail(email: String): Int
}
