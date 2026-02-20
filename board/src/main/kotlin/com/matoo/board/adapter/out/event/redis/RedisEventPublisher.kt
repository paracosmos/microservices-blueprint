package com.matoo.board.adapter.out.event.redis

import com.matoo.core.event.EventSerde
import com.matoo.core.event.EventPublisher
import com.matoo.core.event.dto.EventDto
import com.matoo.core.event.dto.EventTopic
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component("redisEventPublisher")
class RedisEventPublisher(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
): EventPublisher {
    override suspend fun publish(
        topic: EventTopic,
        data: EventDto
    ): Boolean {
        val key = topic.value
        val json = EventSerde.serializeEnvelope(topic,data)
        return redisTemplate
            .opsForStream<String, String>()
            .add(key, mapOf("payload" to json))
            .thenReturn(true)
            .onErrorReturn(false)
            .awaitSingle()
    }
}
