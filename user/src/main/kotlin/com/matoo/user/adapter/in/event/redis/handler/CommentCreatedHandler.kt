package com.matoo.user.adapter.`in`.event.redis.handler

import com.matoo.core.event.EventHandler
import com.matoo.core.event.TopicHandler
import com.matoo.core.event.dto.CommentCreatedEvent
import com.matoo.core.event.dto.UserEventTopic
import org.springframework.stereotype.Component

@Component
class CommentCreatedHandler() : EventHandler<CommentCreatedEvent> {
    override suspend fun handle(event: CommentCreatedEvent): Boolean {
        println("handle $event")
        return true
    }
}
