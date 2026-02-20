package com.matoo.user.adapter.out.client

import com.matoo.user.application.port.out.WebPushSenderPort
import com.matoo.user.domain.model.notification.PushSubscription
import com.matoo.user.infrastructure.config.webpush.WebPushProperties
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Utils
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.stereotype.Component
import java.security.Security

@Component
class WebPushClientAdapter(
    props: WebPushProperties
) : WebPushSenderPort {

    private val pushService: PushService

    init {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
        pushService = PushService().apply {
            subject = props.vapid.subject
            publicKey = Utils.loadPublicKey(props.vapid.publicKey)
            privateKey = Utils.loadPrivateKey(props.vapid.privateKey)
        }
    }

    override suspend fun send(subscription: PushSubscription, payloadJson: String): Boolean {
        val notification = Notification(
            subscription.endpoint,
            subscription.p256dh,
            subscription.auth,
            payloadJson.toByteArray()
        )
        val response = pushService.send(notification)
        // todo
        return response.statusLine.statusCode in 200..299
    }
}
