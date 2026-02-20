package com.matoo.user.adapter.out.client

import com.matoo.user.application.port.out.EmailSenderPort
import com.matoo.user.domain.model.notification.EmailMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

class GmailSmtpEmailClientAdapter(
    private val mailSender: JavaMailSender,
    private val defaultFrom: String
) : EmailSenderPort {

    override suspend fun send(message: EmailMessage) {
//        require(message.to.isNotBlank()) { "EmailMessage.to is blank" }
//        require(message.subject.isNotBlank()) { "EmailMessage.subject is blank" }

        withContext(Dispatchers.IO) {
            val mime = mailSender.createMimeMessage()
            // multipart=true: HTML/첨부 확장 대비 (첨부 없으면 큰 오버헤드는 없음)
            val helper = MimeMessageHelper(mime, true, "UTF-8")
            helper.setTo(message.to)
            helper.setSubject(message.subject)
            helper.setText(message.body, message.isHtml)
            // from: message.from 우선, 없으면 defaultFrom
            helper.setFrom(message.from?.takeIf { it.isNotBlank() } ?: defaultFrom)
            message.replyTo?.takeIf { it.isNotBlank() }?.let { helper.setReplyTo(it) }
            mailSender.send(mime)
        }
    }
}
