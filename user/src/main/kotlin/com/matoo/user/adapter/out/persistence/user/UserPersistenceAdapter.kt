package com.matoo.user.adapter.out.persistence.user

import com.matoo.core.constant.IdType
import com.matoo.user.application.port.out.UserCommandPort
import com.matoo.user.application.port.out.UserQueryPort
import com.matoo.user.domain.model.user.User
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserPersistenceAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserCommandPort, UserQueryPort {
    override fun save(data: User): User {
        val entity = userMapper.toEntity(user = data)
        val saved = userJpaRepository.save(entity)
        return userMapper.toDomain(saved)
    }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)?.let { userMapper.toDomain(it) }
    }

    override fun findById(id: String): User? {
        val user = userJpaRepository.findByUserId(id)
        return user?.let { userMapper.toDomain(it) }
    }


    override fun existsByEmail(email: String): Boolean {
        return userJpaRepository.existsByEmail(email)
    }

    override fun restoreByEmail(email: String): Int {
        return userJpaRepository.restoreByEmail(email)
    }

    @Transactional
    override fun deleteById(id: IdType) {
        userJpaRepository.softDeleteById(id)
    }
}
