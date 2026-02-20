package com.matoo.board.adapter.out.persistence.cache

import com.matoo.board.domain.model.Post
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.core.constant.IdType
import com.matoo.core.constant.TraceConstants
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PostRedisAdapter(
    @Qualifier("postRedisTemplate")
    private val postRedisTemplate: RedisTemplate<String, Post>
) : PostQueryPort, PostCommandPort {

    override fun findById(id: IdType): Post? {
        val post = postRedisTemplate.opsForValue().get("post:$id")
        return post
    }

    override fun save(data: Post): Post {
        postRedisTemplate.opsForValue().set(
            "post:${data.postId}", data,
            TraceConstants.TTL_MINUTES,
            TimeUnit.MINUTES
        )
        return data
    }

    override fun deleteById(id: IdType) {
        postRedisTemplate.delete("post:$id")
    }

    override fun findAll(): List<Post> {
        return emptyList()
    }

    fun deleteAll() {
        val keys = postRedisTemplate.keys("post::*")
        if (keys.isNotEmpty()) {
            postRedisTemplate.delete(keys)
        }
    }
}
