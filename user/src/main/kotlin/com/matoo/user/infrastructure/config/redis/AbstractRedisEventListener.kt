package com.matoo.user.infrastructure.config.redis

import com.matoo.core.constant.TraceConstants
import com.matoo.core.event.dto.EventDto
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.data.redis.connection.stream.Consumer
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.ReadOffset
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import reactor.core.publisher.Mono
import java.util.UUID

abstract class AbstractRedisEventListener<T : EventDto>(
    private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>,
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
) : DisposableBean {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val consumerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    protected abstract val streamKeys: List<String>
    protected abstract val group: String
    protected abstract suspend fun handleMessage(streamKey: String, fields: Map<String, String>): Boolean

    @PostConstruct
    fun consumeStream() {
        consumerScope.launch {
            val consumerName = UUID.randomUUID().toString() // TODO 인스턴스명으로 그룹

            streamKeys.forEach { streamKey ->
                runCatching {
                    redisTemplate
                        .opsForStream<String, String>()
                        .createGroup(streamKey, ReadOffset.from(TraceConstants.READ_OFFSET), group)
                        .awaitSingleOrNull()
                }
            }

            val flows: List<Flow<MapRecord<String, String, String>>> =
                streamKeys.map { streamKey ->
                    streamReceiver
                        .receive(
                            Consumer.from(group, consumerName),
                            StreamOffset.create(streamKey, ReadOffset.lastConsumed())
                        )
                        .onErrorResume { Mono.empty() }
                        .asFlow()
                }

            val ops = redisTemplate.opsForStream<String, String>()

            merge(*flows.toTypedArray()).collect { message ->
                if (!currentCoroutineContext().isActive) return@collect

                val streamKey = message.stream
                if (streamKey == null) {
                    logger.warn("Skipping record with null stream key: {}", message.id)
                    return@collect
                }
                val fields = message.value
                val recordId = message.id

                try {
                    val success = handleMessage(streamKey, fields)
                    if (success) {
                        ops.acknowledge(streamKey, group, recordId)
                            .onErrorResume { Mono.empty() }
                            .awaitSingleOrNull()

                        ops.delete(streamKey, recordId)
                            .onErrorResume { Mono.empty() }
                            .awaitSingleOrNull()
                    }
                    // 실패면 ack 안 해서 pending에 남김(재처리/리트라이 정책 가능)
                } catch (e: Exception) {
                    logger.error("Failed to handle stream message {} on {}", recordId, streamKey, e)
                    // 예외도 ack 안 하면 pending 유지
                }
            }

        }
    }

    override fun destroy() {
        consumerScope.cancel()
    }
}
