package com.matoo.core.event

import com.matoo.core.event.dto.EventDto
import com.matoo.core.event.dto.EventTopic

interface EventPublisher {
    suspend fun publish(topic: EventTopic, data: EventDto): Boolean
}
