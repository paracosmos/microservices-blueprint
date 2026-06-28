package com.matoo.board.adapter.out

import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.board.domain.model.Post
import com.matoo.core.constant.IdType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class PostAdapter(
    @Qualifier("postRepositoryAdapter") private val repositoryPort: PostQueryPort,
    @Qualifier("postLocalCacheAdapter") private val localCacheQueryPort: PostQueryPort,
    @Qualifier("postLocalCacheAdapter") private val localCacheCommandPort: PostCommandPort,
    @Qualifier("postRedisAdapter") private val redisQueryPort: PostQueryPort,
    @Qualifier("postRedisAdapter") private val redisCommandPort: PostCommandPort
) : PostQueryPort {
    override fun findById(id: IdType): Post? {
        return localCacheQueryPort.findById(id)
            ?: redisQueryPort.findById(id)?.also {
                localCacheCommandPort.save(it)
            }
            ?: repositoryPort.findById(id)?.also {
                localCacheCommandPort.save(it)
                redisCommandPort.save(it)
            }
    }
}
