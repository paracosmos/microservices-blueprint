package com.matoo.board.adapter.out.persistence.post

import com.matoo.board.domain.model.Post
import org.springframework.stereotype.Component

@Component
class PostMapper {

    fun toEntity(data: Post): PostEntity {
        return PostEntity(
            postId = data.postId,
            userId = data.userId,
            title = data.title,
            content = data.content,
            createdAt = data.createdAt,
            updatedAt = data.updatedAt
        )
    }

    fun toDomain(data: PostEntity): Post {
        return Post(
            postId = data.postId,
            userId = data.userId,
            title = data.title,
            content = data.content,
            createdAt = data.createdAt,
            updatedAt = data.updatedAt
        )
    }
}
