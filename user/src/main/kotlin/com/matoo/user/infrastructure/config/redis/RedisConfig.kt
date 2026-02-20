package com.matoo.user.infrastructure.config.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.stream.StreamMessageListenerContainer
import org.springframework.data.redis.stream.StreamReceiver
import java.time.Duration

@Configuration
class RedisConfig {
    @Bean
    fun streamReceiver(factory: ReactiveRedisConnectionFactory): StreamReceiver<String, MapRecord<String, String, String>> {
        val options = StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofSeconds(30))
            .build()
        return StreamReceiver.create(factory, options)
    }

//    @Bean(destroyMethod = "stop")
//    fun streamMessageListenerContainer(
//        connectionFactory: RedisConnectionFactory
//    ): StreamMessageListenerContainer<String, MapRecord<String, String, String>> {
//
//        val options: StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
//                String, MapRecord<String, String, String>
//                > = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
//            .builder()
//            .pollTimeout(Duration.ofSeconds(30))
//            .build()
//
//        return StreamMessageListenerContainer.create(connectionFactory, options)
//            .also { it.start() }
//    }
}
