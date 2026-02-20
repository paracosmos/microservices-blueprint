package com.matoo.user.adapter.`in`.event

import com.matoo.core.event.TopicHandler
import org.springframework.stereotype.Component

@Component
class UserEventDispatcher(
    handlers: List<TopicHandler>
) {
    private val byTopic = handlers.associateBy { it.topic }
    suspend fun dispatch(streamKey: String, fields: Map<String, String>): Boolean {
        val h = byTopic[streamKey] ?: return false
        val json = fields["payload"] ?: return false
        return h.handleJson(json)
    }
}
