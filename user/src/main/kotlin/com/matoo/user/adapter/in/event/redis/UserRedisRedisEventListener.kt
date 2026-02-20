package com.matoo.user.adapter.`in`.event.redis

import com.matoo.core.constant.RemoteService
import com.matoo.user.infrastructure.config.redis.AbstractRedisEventListener
import com.matoo.core.event.dto.UserEventDto
import com.matoo.core.event.dto.UserEventTopic
import com.matoo.user.adapter.`in`.event.UserEventDispatcher
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class UserRedisRedisEventListener(
    streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>,
    redisTemplate: ReactiveRedisTemplate<String, String>,
    private val userEventDispatcher: UserEventDispatcher,
) : AbstractRedisEventListener<UserEventDto>(streamReceiver, redisTemplate) {
    override val streamKeys: List<String> = UserEventTopic.entries.map { it.value }
    override val group: String = RemoteService.USER.name
    override suspend fun handleMessage(streamKey: String, fields: Map<String, String>): Boolean =
        userEventDispatcher.dispatch(streamKey, fields)
}
