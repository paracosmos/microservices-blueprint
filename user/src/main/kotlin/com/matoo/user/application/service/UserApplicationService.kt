package com.matoo.user.application.service

import com.matoo.core.constant.IdType
import com.matoo.core.support.exception.requireStatus
import com.matoo.core.util.CoreUtil
import com.matoo.user.application.model.LocalSigninCommand
import com.matoo.user.application.model.SigninQuery
import com.matoo.user.application.model.LocalSignupCommand
import com.matoo.user.application.model.OAuthSigninCommand
import com.matoo.user.application.port.`in`.UserUseCase
import com.matoo.user.application.port.out.UserCommandPort
import com.matoo.user.application.port.out.UserQueryPort
import com.matoo.user.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserApplicationService(
    private val userCommandPort: UserCommandPort,
    private val userQueryPort: UserQueryPort,
    private val passwordEncoder: PasswordEncoder,
//    @PersistenceContext private val em: EntityManager
) : UserUseCase {

    override suspend fun signupLocal(command: LocalSignupCommand): Boolean = withContext(Dispatchers.IO) {
        val user = userQueryPort.findByEmail(command.email)
            ?: User.create(
                userId = CoreUtil.generateId(),
                email = command.email,
                name = command.name
            )
        val isNew = user.registerLocal(
            encodedPassword = passwordEncoder.encode(command.password)
        )
        saveUser(user)
        isNew
    }

    override suspend fun signinLocal(command: LocalSigninCommand): SigninQuery = withContext(Dispatchers.IO) {
        val user = userQueryPort.findByEmail(command.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        try {
            user.verifyPassword(
                raw = command.password,
                matcher = { raw, encoded -> passwordEncoder.matches(raw, encoded) }
            )
        } catch (_: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
        SigninQuery(
            userId = user.userId,
            email = user.email
        )
    }

    override suspend fun signinOAuth(command: OAuthSigninCommand): SigninQuery = withContext(Dispatchers.IO) {
        val user = userQueryPort.findByEmail(command.email)
            ?: User.create(
                userId = CoreUtil.generateId(),
                email = command.email,
                name = null
            )
        val touched = user.registerOAuth(
            oauthProvider = command.oauthProvider,
            providerUid = command.providerUid,
            providerName = command.providerName,
            providerPicture = command.providerPicture
        )
        if (touched) {
            saveUser(user)
        }
        SigninQuery(
            userId = user.userId,
            email = user.email
        )
    }

    override suspend fun signoff(userId: IdType) {
        withContext(Dispatchers.IO) { userCommandPort.deleteById(userId) }
    }

    override suspend fun restore(email: String) {
        withContext(Dispatchers.IO) {
            userQueryPort
                .existsByEmail(email)
                .takeIf { it }
                ?.run { userQueryPort.restoreByEmail(email) }
        }
    }

    private fun saveUser(user: User) {
        try {
            userCommandPort.save(user)
        } catch (e: DataIntegrityViolationException) {
            val x = userQueryPort.existsByEmail(user.email)
            requireStatus(!x, HttpStatus.CONFLICT) { "ACCOUNT_RECOVERY_REQUIRED" }
            throw e
        }
    }
}
