package com.matoo.board.application.service.post

import com.matoo.board.application.port.`in`.PostUseCase
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.board.domain.model.Post
import com.matoo.core.support.exception.orNotFound
import com.matoo.core.util.CoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class PostService(
    @Qualifier("postRepositoryAdapter")
    private val postCommandPort: PostCommandPort,
    @Qualifier("postRepositoryAdapter")
    private val postQueryPort: PostQueryPort,
) : PostUseCase {

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
        postCommandPort.save(updated)
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
    }
}
