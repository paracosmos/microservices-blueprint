package com.matoo.user.adapter.`in`.api

import com.matoo.core.dto.user.LocalSigninRequest
import com.matoo.core.dto.user.SigninResponse
import com.matoo.core.dto.user.OAuthSigninRequest
import com.matoo.user.application.model.LocalSigninCommand
import com.matoo.user.application.model.toCommand
import com.matoo.user.application.model.toResponse
import com.matoo.user.application.port.`in`.UserUseCase
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/user/internal")
class UserInternalController(
    private val userUseCase: UserUseCase
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/signin/local")
    suspend fun signinLocal(@RequestBody req: LocalSigninRequest): ResponseEntity<SigninResponse> {
        logger.info("signinLocal $req")
        val res = userUseCase.signinLocal(
            LocalSigninCommand(email = req.email.value, password = req.password.value)
        )
        val body = res.toResponse()
        return ResponseEntity.ok(body)
    }

    @PostMapping("/signin/oauth")
    suspend fun signinOAuth(@RequestBody req: OAuthSigninRequest): ResponseEntity<SigninResponse> {
        logger.info("oauth $req")
        val res = userUseCase.signinOAuth(req.toCommand())
        val body = res.toResponse()
        return ResponseEntity.ok(body)
    }
}
