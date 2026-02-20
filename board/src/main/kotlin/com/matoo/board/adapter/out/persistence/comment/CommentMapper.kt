package com.matoo.board.adapter.out.persistence.comment

import com.matoo.board.domain.model.Comment
import org.springframework.stereotype.Component

@Component
class CommentMapper {

    fun toEntity(
        data: Comment,
    ): CommentEntity {
        return CommentEntity(
            commentId = data.commentId,
            userId = data.userId,
            content = data.content,
            createdAt = data.createdAt,
            updatedAt = data.updatedAt,
            postId = data.postId
        )
    }

    fun toDomain(entity: CommentEntity): Comment {
        return Comment(
            commentId = entity.commentId,
            postId = entity.postId,
            userId = entity.userId,
            content = entity.content,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
