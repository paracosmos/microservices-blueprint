package com.matoo.user.adapter.`in`.web

import com.matoo.core.constant.TraceConstants
import com.matoo.user.adapter.`in`.web.dto.PushSubscribeRequest
import com.matoo.user.application.model.PushSubscribeCommand
import com.matoo.user.application.port.`in`.PushUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/push")
class PushController(
    private val pushUseCase: PushUseCase
) {

    @PostMapping("/subscribe")
    suspend fun subscribe(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @RequestBody req: PushSubscribeRequest
    ): ResponseEntity<Void> {
        pushUseCase.subscribe(
            userId,
            PushSubscribeCommand(
                endpoint = req.endpoint,
                p256dh = req.keys.p256dh,
                auth = req.keys.auth
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/subscribe")
    suspend fun unsubscribe(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @RequestParam endpoint: String
    ): ResponseEntity<Void> {
        pushUseCase.unsubscribe(userId, endpoint)
        return ResponseEntity.noContent().build()
    }

    // 테스트용
    @PostMapping("/test-send")
    suspend fun testSend(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
    ): ResponseEntity<Void> {
        pushUseCase.sendToUser(userId, "테스트", "Web Push 도착!")
        return ResponseEntity.ok().build()
    }
}
