package com.matoo.user.application.port.out

import com.matoo.user.domain.model.notification.EmailMessage

interface EmailSenderPort {
    suspend fun send(message: EmailMessage)
}
