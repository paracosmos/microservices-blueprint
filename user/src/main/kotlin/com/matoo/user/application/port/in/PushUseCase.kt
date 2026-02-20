package com.matoo.user.application.port.`in`

import com.matoo.user.application.model.PushSubscribeCommand

interface PushUseCase {
    suspend fun subscribe(userId: String, command: PushSubscribeCommand)
    suspend fun unsubscribe(userId: String, endpoint: String)
    suspend fun sendToUser(userId: String, title: String, body: String)
}
