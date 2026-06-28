package com.matoo.board.application.service.post

import com.matoo.board.application.port.`in`.PostUseCase
import com.matoo.board.application.port.out.PostCachePort
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.board.domain.model.Post
import com.matoo.core.support.exception.orNotFound
import com.matoo.core.util.CoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class PostService(
    @Qualifier("postRepositoryAdapter")
    private val postCommandPort: PostCommandPort,
    @Qualifier("postAdapter")
    private val postQueryPort: PostQueryPort,
    private val postCachePort: PostCachePort,

    ) : PostUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun create(
        userId: String,
        title: String,
        content: String
    ): Post {
        val post = Post(
            postId = CoreUtil.generateId(),
            userId = userId,
            title = title,
            content = content,
            createdAt = Instant.now(),
        )

        return withContext(Dispatchers.IO) { postCommandPort.save(post) }
    }

    override suspend fun update(
        postId: String,
        userId: String,
        title: String,
        content: String
    ): Post = withContext(Dispatchers.IO) {
        val post = postQueryPort.findById(postId).orNotFound()
        if (post.userId != userId) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.reasonPhrase
            )
        }
        val updated = post.copy(
            title = title,
            content = content,
            updatedAt = Instant.now()
        )
        val saved = postCommandPort.save(updated)
        evictQuietly(postId)
        saved
    }

    override suspend fun delete(postId: String, userId: String): Unit = withContext(Dispatchers.IO) {
        val post = postQueryPort.findById(postId).orNotFound()
        if (post.userId != userId) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.reasonPhrase
            )
        }
        postCommandPort.deleteById(postId)
        evictQuietly(postId)
    }

    // 영속화는 이미 커밋됐으므로 캐시 무효화 실패가 요청을 실패시키면 안 된다(best-effort + 경고 로그).
    private fun evictQuietly(postId: String) {
        runCatching { postCachePort.evict(postId) }
            .onFailure { logger.warn("post cache evict failed for postId={}", postId, it) }
    }
}
