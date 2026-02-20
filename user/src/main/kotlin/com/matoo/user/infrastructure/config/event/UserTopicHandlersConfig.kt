package com.matoo.user.infrastructure.config.event

import com.matoo.core.event.AbstractTopicHandler
import com.matoo.core.event.TopicHandler
import com.matoo.core.event.dto.CommentCreatedEvent
import com.matoo.core.event.dto.UserEventTopic
import com.matoo.user.adapter.`in`.event.redis.handler.CommentCreatedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserTopicHandlersConfig {

    @Bean
    fun commentCreatedTopicHandler(
        handler: CommentCreatedHandler
    ): TopicHandler =
        object : AbstractTopicHandler<CommentCreatedEvent>(
            topic = UserEventTopic.COMMENT_CREATED.value,
            dataType = CommentCreatedEvent::class.java,
            handler = handler
        ) {}
}
