package com.matoo.board.infrastructure.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.matoo.board.domain.model.Post
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    private val objectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule.Builder().build())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @Bean
    fun postRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, Post> {
        val valueSerializer = Jackson2JsonRedisSerializer(objectMapper, Post::class.java)
        return RedisTemplate<String, Post>().apply {
            this.connectionFactory = connectionFactory
            this.keySerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.valueSerializer = valueSerializer
            this.hashValueSerializer = valueSerializer
            afterPropertiesSet()
        }
    }

    @Bean("customStringRedisTemplate")
    fun customStringRedisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = connectionFactory
        val stringSerializer = StringRedisSerializer()
        template.keySerializer = stringSerializer
        template.valueSerializer = stringSerializer
        template.hashKeySerializer = stringSerializer
        template.hashValueSerializer = stringSerializer
        template.afterPropertiesSet()
        return template
    }

    @Bean("genericRedisTemplate")
    fun genericRedisTemplate(
        connectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.keySerializer = keySerializer
        template.hashKeySerializer = keySerializer
        template.valueSerializer = valueSerializer
        template.hashValueSerializer = valueSerializer
        template.afterPropertiesSet()
        return template
    }


    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory
        return template
    }

    //    @Bean
//    fun streamReceiver(factory: ReactiveRedisConnectionFactory): StreamReceiver<String, MapRecord<String, String, String>> {
//        val options = StreamReceiver.StreamReceiverOptions.builder()
//            .pollTimeout(Duration.ofSeconds(1))
//            .build()
//        return StreamReceiver.create(factory, options)
//    }
}
