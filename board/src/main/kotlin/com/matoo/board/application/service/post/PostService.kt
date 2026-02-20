package com.matoo.board.application.service.post

import com.matoo.board.application.port.`in`.PostUseCase
import com.matoo.board.application.port.out.PostCachePort
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.board.domain.model.Post
import com.matoo.core.support.exception.orNotFound
import com.matoo.core.util.CoreUtil
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

    override fun create(
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

        return postCommandPort.save(post)
    }

    override fun get(postId: String): Post {
        return postQueryPort.findById(postId).orNotFound()
    }

    override fun getAll(): List<Post> {
        return postQueryPort.findAll()
    }

    override fun update(
        postId: String,
        userId: String,
        title: String,
        content: String
    ): Post {
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
        postCachePort.evict(postId)
        return saved
    }

    override fun delete(postId: String, userId: String) {
        val post = postQueryPort.findById(postId).orNotFound()
        if (post.userId != userId) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.reasonPhrase
            )
        }
        postCommandPort.deleteById(postId)
        postCachePort.evict(postId)
    }
}
