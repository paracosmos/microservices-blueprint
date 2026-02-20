package com.matoo.board.infrastructure.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.matoo.core.constant.TraceConstants
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    @Primary
    fun caffeineCacheManager(): CacheManager {
        return CaffeineCacheManager("post").apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(1_000)
                    .expireAfterWrite(TraceConstants.TTL_MINUTES, TimeUnit.MINUTES)
            )
        }
    }
}
