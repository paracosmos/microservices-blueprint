package com.matoo.board.application.service.comment

import com.matoo.board.application.port.`in`.CommentUseCase
import com.matoo.board.application.port.out.CommentCommandPort
import com.matoo.board.application.port.out.CommentQueryPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.board.domain.model.Comment
import com.matoo.core.event.EventPublisher
import com.matoo.core.event.dto.CommentCreatedEvent
import com.matoo.core.event.dto.UserEventTopic
import com.matoo.core.support.exception.orNotFound
import com.matoo.core.util.CoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentCommandPort: CommentCommandPort,
    private val commentQueryPort: CommentQueryPort,
    @Qualifier("postRepositoryAdapter")
    private val repositoryPort: PostQueryPort,
    @Qualifier("redisEventPublisher")
    private val eventPublisher: EventPublisher
) : CommentUseCase {
    override suspend fun addComment(
        postId: String,
        userId: String,
        content: String
    ): Comment {
        withContext(Dispatchers.IO) { repositoryPort.findById(postId) }.orNotFound()
        val comment = Comment(
            commentId = CoreUtil.generateId(),
            postId = postId,
            userId = userId,
            content = content
        )
        val saved = withContext(Dispatchers.IO) { commentCommandPort.save(comment) }
        // 저장이 성공한 뒤에 이벤트를 발행해야 유령 알림/카운트 불일치를 막을 수 있다.
        eventPublisher.publish(
            topic = UserEventTopic.COMMENT_CREATED,
            data = CommentCreatedEvent(
                commentId = saved.commentId,
                postId = saved.postId,
                userId = saved.userId,
            )
        )
        return saved
    }

    override suspend fun getByPost(postId: String): List<Comment> {
        return withContext(Dispatchers.IO) { commentQueryPort.findByPostId(postId) }
    }
}
