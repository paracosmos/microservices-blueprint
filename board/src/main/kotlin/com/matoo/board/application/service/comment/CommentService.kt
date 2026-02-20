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
        repositoryPort.findById(postId).orNotFound()
        val comment = Comment(
            commentId = CoreUtil.generateId(),
            postId = postId,
            userId = userId,
            content = content
        )
        eventPublisher.publish(
            topic = UserEventTopic.COMMENT_CREATED,
            data = CommentCreatedEvent(
                commentId = comment.commentId,
                postId = comment.postId,
                userId = comment.userId,
            )
        )
        return commentCommandPort.save(comment)
    }

    override fun getByPost(postId: String): List<Comment> {
        return commentQueryPort.findByPostId(postId)
    }
}
