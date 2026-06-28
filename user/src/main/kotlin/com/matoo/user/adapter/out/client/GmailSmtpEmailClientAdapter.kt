package com.matoo.user.adapter.out.client

import com.matoo.core.support.exception.requireStatus
import com.matoo.user.application.port.out.EmailSenderPort
import com.matoo.user.domain.model.notification.EmailMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

class GmailSmtpEmailClientAdapter(
    private val mailSender: JavaMailSender,
    private val defaultFrom: String
) : EmailSenderPort {

    override suspend fun send(message: EmailMessage) {
        // 입력 검증 실패는 400으로 매핑(전역 IllegalArgumentException 핸들러로 5xx 를 가리지 않도록 타입을 좁힌다).
        requireStatus(message.to.isNotBlank(), HttpStatus.BAD_REQUEST) { "EmailMessage.to is blank" }
        requireStatus(message.subject.isNotBlank(), HttpStatus.BAD_REQUEST) { "EmailMessage.subject is blank" }

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
