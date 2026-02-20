package com.matoo.user.application.port.out

import com.matoo.user.domain.model.notification.PushSubscription

interface WebPushSenderPort {
    /**
     * @return true if delivered (2xx). false if subscription is invalid/expired (404/410 등) or hard failure.
     */
    suspend fun send(subscription: PushSubscription, payloadJson: String): Boolean
}
