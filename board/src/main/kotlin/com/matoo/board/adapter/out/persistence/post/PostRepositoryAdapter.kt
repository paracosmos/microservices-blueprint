package com.matoo.board.adapter.out.persistence.post

import com.matoo.board.domain.model.Post
import com.matoo.board.application.port.out.PostCommandPort
import com.matoo.board.application.port.out.PostQueryPort
import com.matoo.core.constant.IdType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component("postRepositoryAdapter")
class PostRepositoryAdapter(
    private val postJpaRepository: PostJpaRepository,
    private val postMapper: PostMapper
) : PostCommandPort, PostQueryPort {

    override fun save(
        data: Post
    ): Post {
        val entity = postMapper.toEntity(data)
        val saved = postJpaRepository.save(entity)
        return postMapper.toDomain(saved)
    }

    override fun findById(id: IdType): Post? {
        val post = postJpaRepository.findByIdOrNull(id)?.let { postMapper.toDomain(it) }
        println("postRepositoryAdapter::findById $post")
        return post
    }

    override fun findAll(): List<Post> {
        return postJpaRepository.findAll().map { postMapper.toDomain(it) }
    }

    @Transactional
    override fun deleteById(id: IdType) {
        // postJpaRepository.deleteById(id)
        postJpaRepository.softDeleteById(id)
    }
}
