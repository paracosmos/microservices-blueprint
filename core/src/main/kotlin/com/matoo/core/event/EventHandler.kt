package com.matoo.core.event

import com.matoo.core.event.dto.EventDto

interface EventHandler<T : EventDto> {
    suspend fun handle(event: T): Boolean
}
