package com.matoo.auth.application.service

import com.matoo.auth.application.model.AuthRequestModel
import com.matoo.auth.application.model.AuthResponseModel
import com.matoo.auth.application.model.OAuthRequestModel
import com.matoo.auth.application.port.`in`.AuthUseCase
import com.matoo.auth.application.port.out.UserClientPort
import com.matoo.auth.application.port.out.OAuthClientPort
import com.matoo.auth.application.port.out.TokenProviderPort
import com.matoo.auth.domain.service.AuthDomainService
import com.matoo.core.constant.EmailType
import com.matoo.core.constant.PasswordType
import com.matoo.core.dto.user.LocalSigninRequest
import com.matoo.core.dto.user.OAuthSigninRequest
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service

@Service
class AuthApplicationService(
    private val authDomainService: AuthDomainService,
    private val tokenProviderPort: TokenProviderPort,
    private val userClientPort: UserClientPort,
    private val oauthClientPort: OAuthClientPort
) : AuthUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun auth(authRequestModel: AuthRequestModel): AuthResponseModel {
        val mdc = MDC.getCopyOfContextMap()
        val res = userClientPort.signinLocal(
            LocalSigninRequest(
                email = EmailType.of(authRequestModel.email),
                password = PasswordType.of(authRequestModel.password)
            )
        )
        //  throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        withContext(MDCContext(mdc)) { logger.info("auth") }
        // todo domain policy
        authDomainService.validateUser(res.userId)
        // todo claims policy
        val claims = listOfNotNull(
            authRequestModel.email.let { "email" to it },
            // roles.takeIf { it.isNotEmpty() }?.let { "roles" to it },
            // roles.takeIf { it.isNotEmpty() }?.let { "roles" to roles.joinToString(",") }).toMap()
        ).toMap()
        val tokenPair = tokenProviderPort.generate(res.userId, claims)
        return AuthResponseModel(
            accessToken = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken
        )
    }

    override suspend fun oauth(oauthRequestModel: OAuthRequestModel): AuthResponseModel {
        val res = oauthClientPort.getExternalUserInfo(
            code = oauthRequestModel.code,
            codeVerifier = oauthRequestModel.codeVerifier,
            provider = oauthRequestModel.provider,
        )
        val singed = userClientPort.signinOAuth(
            OAuthSigninRequest(
                oauthProvider = oauthRequestModel.provider,
                email = EmailType.of(res.email),
                providerUid = res.id,
                providerName = res.name,
                providerPicture = res.picture,
            )
        )
        authDomainService.validateUser(singed.userId)
        val claims = mapOf(
            "email" to res.email,
            "provider" to oauthRequestModel.provider.name
        )
        val tokenPair = tokenProviderPort.generate(singed.userId, claims)
        return AuthResponseModel(tokenPair.accessToken, tokenPair.refreshToken)
    }


}
