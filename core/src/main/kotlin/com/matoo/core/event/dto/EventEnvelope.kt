package com.matoo.core.event.dto

import com.matoo.core.util.CoreUtil
import java.time.Instant

data class EventEnvelope<T : EventDto>(
    val eventId: String = CoreUtil.generateId(),
    val occurredAt: Instant = Instant.now(),
    val type: String,
    val version: Int = 1,
    val data: T
)
