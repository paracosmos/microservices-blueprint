package com.matoo.board.adapter.out.persistence.cache

import com.matoo.board.domain.model.Post
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.core.constant.IdType
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component("postLocalCacheAdapter")
@CacheConfig(cacheNames = ["post"])
class PostLocalCacheAdapter : PostQueryPort, PostCommandPort {

    // @Cacheable(cacheNames = ["post"], key = "#id")
    @Cacheable(key = "#id")
    override fun findById(id: IdType): Post? {
        println("postLocalCacheAdapter::findById id=$id")
        return null
    }

    @CachePut(key = "#data.postId")
    override fun save(data: Post): Post {
        println("postLocalCacheAdapter::save $data")
        return data
    }

    override fun findAll(): List<Post> {
        return emptyList()
    }

    @CacheEvict(key = "#id")
    override fun deleteById(id: IdType) {
        println("postLocalCacheAdapter::deleteById id=$id")
    }

    @CacheEvict(
        cacheNames = ["post"],
        allEntries = true
    )
    fun deleteAll() {
        println("postLocalCacheAdapter::deleteAll")
    }
}
