package com.matoo.user.adapter.`in`.event.redis.handler

import com.matoo.core.event.EventHandler
import com.matoo.core.event.dto.CommentCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CommentCreatedHandler() : EventHandler<CommentCreatedEvent> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun handle(event: CommentCreatedEvent): Boolean {
        logger.debug("handle {}", event)
        return true
    }
}
