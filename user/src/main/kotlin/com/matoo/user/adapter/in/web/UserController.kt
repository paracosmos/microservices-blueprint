package com.matoo.user.adapter.`in`.web

import com.matoo.core.constant.TraceConstants
import com.matoo.user.adapter.`in`.web.dto.LocalSignupRequest
import com.matoo.user.adapter.`in`.web.dto.RestoreRequest
import com.matoo.user.application.model.LocalSignupCommand
import com.matoo.user.application.port.`in`.UserUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userUseCase: UserUseCase
) {

    // todo internal
    @PostMapping("/signup/local")
    suspend fun signupLocal(
        @Valid
        @RequestBody req: LocalSignupRequest
    ): ResponseEntity<Void> {
        val isNew = userUseCase.signupLocal(
            LocalSignupCommand(
                email = req.email.value,
                password = req.password.value,
                name = req.name
            )
        )
        val status = if (isNew) HttpStatus.CREATED else HttpStatus.OK
        return ResponseEntity.status(status).build()
    }

    @DeleteMapping("/signoff")
    suspend fun signoff(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
    ): ResponseEntity<Void> {
        userUseCase.signoff(userId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/restore")
    suspend fun restore(
        @Valid @RequestBody req: RestoreRequest
    ): ResponseEntity<Void> {
        userUseCase.restore(req.email.value)
        return ResponseEntity.ok().build()
    }
}
