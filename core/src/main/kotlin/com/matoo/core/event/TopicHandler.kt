package com.matoo.core.event

interface TopicHandler {
    val topic: String
    suspend fun handleJson(json: String): Boolean
}
