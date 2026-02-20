package com.matoo.user.adapter.`in`.web

import com.matoo.user.adapter.`in`.web.dto.EmailRequest
import com.matoo.user.application.port.`in`.EmailUseCase
import com.matoo.user.domain.model.notification.EmailMessage
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/v1/user/email")
class EmailController(
    private val emailUseCase: EmailUseCase
) {

    @PostMapping("/send")
    suspend fun send(
        @RequestBody req: EmailRequest
    ): ResponseEntity<Void> {
        emailUseCase.send(
            EmailMessage(
                to = req.to,
                subject = req.subject,
                body = req.body,
                isHtml = req.isHtml
            )
        )
        return ResponseEntity.ok().build()
    }
}
