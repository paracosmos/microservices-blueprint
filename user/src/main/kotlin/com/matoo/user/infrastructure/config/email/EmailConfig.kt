package com.matoo.user.infrastructure.config.email

import com.matoo.user.adapter.out.client.GmailSmtpEmailClientAdapter
import com.matoo.user.adapter.out.client.SesEmailClientAdapter
import com.matoo.user.application.port.out.EmailSenderPort
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import software.amazon.awssdk.services.sesv2.SesV2Client

@Configuration
@EnableConfigurationProperties(EmailProperties::class)
class EmailConfig(
    private val props: EmailProperties
) {
    @Bean("smtpEmailSender")
    @ConditionalOnProperty(prefix = "email.smtp", name = ["enabled"], havingValue = "true")
    fun smtpSender(mailSender: JavaMailSender): EmailSenderPort =
        GmailSmtpEmailClientAdapter(
            mailSender = mailSender,
            defaultFrom = props.smtp.from
        )

    @Bean("sesEmailSender")
    @ConditionalOnProperty(prefix = "email.ses", name = ["enabled"], havingValue = "true")
    fun sesSender(ses: SesV2Client): EmailSenderPort =
        SesEmailClientAdapter(
            ses = ses,
            defaultFrom = props.ses.from
        )
}
