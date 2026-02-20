package com.matoo.user.application.service

import com.matoo.user.adapter.out.template.ThymeleafTemplateRenderer
import com.matoo.user.application.port.`in`.EmailUseCase
import com.matoo.user.application.port.out.EmailSenderPort
import com.matoo.user.domain.model.notification.EmailMessage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class EmailApplicationService(
    @Qualifier("smtpEmailSender")
    private val smtpEmailSender: EmailSenderPort,
//    @Qualifier("sesEmailSender")
//    private val sesEmailSender: EmailSenderPort,
    private val templateRenderer: ThymeleafTemplateRenderer
): EmailUseCase {

    override suspend fun send(message: EmailMessage) {
        smtpEmailSender.send(message)
    }

    override suspend fun sendCommentNotificationMail(
        to: String,
        receiverName: String,
        postTitle: String,
        commenterName: String,
        commentContent: String,
        postUrl: String
    ) {
        val html = templateRenderer.render(
            template = "mail/comment-notification",
            variables = mapOf(
                "receiverName" to receiverName,
                "postTitle" to postTitle,
                "commenterName" to commenterName,
                "commentContent" to commentContent,
                "postUrl" to postUrl
            )
        )
        smtpEmailSender.send(
            EmailMessage(
                to = to,
                subject = "댓글이 달렸어요",
                body = html,
                isHtml = true
            )
        )
    }
}
