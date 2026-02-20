package com.matoo.user.adapter.out.client

import com.matoo.user.application.port.out.EmailSenderPort
import com.matoo.user.domain.model.notification.EmailMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.services.sesv2.SesV2Client
import software.amazon.awssdk.services.sesv2.model.Body
import software.amazon.awssdk.services.sesv2.model.Content
import software.amazon.awssdk.services.sesv2.model.Destination
import software.amazon.awssdk.services.sesv2.model.EmailContent
import software.amazon.awssdk.services.sesv2.model.Message
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest

class SesEmailClientAdapter(
    private val ses: SesV2Client,
    private val defaultFrom: String
) : EmailSenderPort {

    override suspend fun send(message: EmailMessage) {
        withContext(Dispatchers.IO) {
            val from = message.from?.takeIf { it.isNotBlank() } ?: defaultFrom

            val subject = Content.builder()
                .data(message.subject)
                .charset("UTF-8")
                .build()

            val bodyContent = Content.builder()
                .data(message.body)
                .charset("UTF-8")
                .build()

            val body = if (message.isHtml) {
                Body.builder().html(bodyContent).build()
            } else {
                Body.builder().text(bodyContent).build()
            }

            val emailContent = EmailContent.builder()
                .simple(
                    Message.builder()
                        .subject(subject)
                        .body(body)
                        .build()
                )
                .build()

            val destination = Destination.builder()
                .toAddresses(message.to)
                .build()

            val reqBuilder = SendEmailRequest.builder()
                .fromEmailAddress(from)
                .destination(destination)
                .content(emailContent)

            // replyTo는 SES에서 replyToAddresses로
            message.replyTo?.takeIf { it.isNotBlank() }?.let { reqBuilder.replyToAddresses(it) }

            ses.sendEmail(reqBuilder.build())
        }
    }
}
