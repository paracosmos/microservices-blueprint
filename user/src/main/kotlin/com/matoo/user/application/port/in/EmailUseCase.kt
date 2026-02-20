package com.matoo.user.application.port.`in`

import com.matoo.user.domain.model.notification.EmailMessage

interface EmailUseCase {
    suspend fun send(message: EmailMessage)

    suspend fun sendCommentNotificationMail(
        to: String,
        receiverName: String,
        postTitle: String,
        commenterName: String,
        commentContent: String,
        postUrl: String
    )
}
