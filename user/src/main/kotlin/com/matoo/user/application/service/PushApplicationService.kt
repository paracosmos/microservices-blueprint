package com.matoo.user.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.matoo.user.application.model.PushSubscribeCommand
import com.matoo.user.application.port.`in`.PushUseCase
import com.matoo.user.application.port.out.PushSubscriptionCommandPort
import com.matoo.user.application.port.out.PushSubscriptionQueryPort
import com.matoo.user.application.port.out.WebPushSenderPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class PushApplicationService(
    private val commandPort: PushSubscriptionCommandPort,
    private val queryPort: PushSubscriptionQueryPort,
    private val senderPort: WebPushSenderPort,
    private val objectMapper: ObjectMapper
) : PushUseCase {

    override suspend fun subscribe(
        userId: String,
        command: PushSubscribeCommand
    ) {
        withContext(Dispatchers.IO) {
            commandPort.upsert(userId, command)
        }
    }

    override suspend fun unsubscribe(userId: String, endpoint: String) {
        withContext(Dispatchers.IO) {
            commandPort.deactivateByEndpoint(userId, endpoint)
        }
    }

    override suspend fun sendToUser(userId: String, title: String, body: String) {
        val subs = withContext(Dispatchers.IO) {
            queryPort.findActiveByUserId(userId)
        }
        if (subs.isEmpty()) return
        val payload = objectMapper.writeValueAsString(
            mapOf(
                "title" to title,
                "body" to body
            )
        )
        // todo 각 subscription에 전송 (병렬 필요하면 async로 확장 가능)
        subs.forEach { sub ->
            val ok = withContext(Dispatchers.IO) {
                senderPort.send(sub, payload)
            }
            // invalid면 비활성화 정리
            if (!ok) {
                withContext(Dispatchers.IO) {
                    commandPort.deactivateByEndpointGlobal(sub.endpoint)
                }
            }
        }
    }


}
