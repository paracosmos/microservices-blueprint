package com.matoo.board.adapter.out.persistence.cache

import com.matoo.board.application.port.out.PostCachePort
import com.matoo.board.domain.model.Post
import com.matoo.core.constant.IdType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class PostCacheAdapter(
    private val cacheManager: CacheManager,
    @Qualifier("postRedisTemplate")
    private val postRedisTemplate: RedisTemplate<String, Post>
) : PostCachePort {

    private val cacheName = "post"

    override fun evict(id: IdType) {
        val key = "$cacheName:$id"
        cacheManager.getCache(cacheName)?.evict(id)
        postRedisTemplate.delete(key)
    }


    override fun evictAll() {
        cacheManager.getCache(cacheName)?.clear()
        val keys = postRedisTemplate.keys("$cacheName::*")
        if (keys.isNotEmpty()) {
            postRedisTemplate.delete(keys)
        }
    }
}
