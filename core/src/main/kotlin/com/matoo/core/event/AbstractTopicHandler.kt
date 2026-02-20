package com.matoo.core.event

import com.matoo.core.event.dto.EventDto

abstract class AbstractTopicHandler<T : EventDto>(
    final override val topic: String,
    private val dataType: Class<T>,
    private val handler: EventHandler<T>
) : TopicHandler {
    final override suspend fun handleJson(json: String): Boolean {
        val env = EventSerde.deserializeEnvelope(json, dataType)
        return handler.handle(env.data)
    }
}
