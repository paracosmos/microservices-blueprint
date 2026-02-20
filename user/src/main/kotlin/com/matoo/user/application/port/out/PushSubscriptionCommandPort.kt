package com.matoo.user.application.port.out

import com.matoo.user.application.model.PushSubscribeCommand

interface PushSubscriptionCommandPort {
    suspend fun upsert(userId: String, command: PushSubscribeCommand)
    suspend fun deactivateByEndpoint(userId: String, endpoint: String)
    suspend fun deactivateByEndpointGlobal(endpoint: String) // 전송 실패 시 정리용
}
