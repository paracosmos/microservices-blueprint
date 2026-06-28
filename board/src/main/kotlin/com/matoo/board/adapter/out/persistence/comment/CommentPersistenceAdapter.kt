package com.matoo.board.adapter.out.persistence.comment

import com.matoo.board.application.port.out.CommentCommandPort
import com.matoo.board.application.port.out.CommentQueryPort
import com.matoo.board.domain.model.Comment
import com.matoo.core.constant.IdType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentPersistenceAdapter(
    private val commentJpaRepository: CommentJpaRepository,
    private val commentMapper: CommentMapper,
//    private val entityManager: EntityManager,
//    private val postRepository: PostRepositoryAdapter,  // postRepository.getReferenceById(data.postId)
) : CommentCommandPort, CommentQueryPort {

    override fun save(data: Comment): Comment {
//        val postEntity = entityManager.getReference(PostEntity::class.java, data.postId)
        val entity = commentMapper.toEntity(data)
        val saved = commentJpaRepository.save(entity)
        return commentMapper.toDomain(saved)
    }

    override fun findById(id: IdType): Comment? {
        return commentJpaRepository.findByIdOrNull(id)?.let { return commentMapper.toDomain(it) }
    }

    @Transactional
    override fun deleteById(id: IdType) {
        commentJpaRepository.softDeleteById(id)
    }
}
