package com.matoo.user.adapter.out.persistence.user

import com.matoo.user.adapter.out.persistence.provider.ProviderEntity
import com.matoo.user.adapter.out.persistence.provider.ProviderMapper
import com.matoo.user.domain.model.user.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toEntity(user: User): UserEntity {
        val userEntity = UserEntity(
            userId = user.userId,
            email = user.email,
            password = user.encodedPassword(),
            name = user.name(),
            createdAt = user.createdAt
        )
        val providerEntities = user.providers().map {
            ProviderEntity(
                providerId = it.providerId,
                user = userEntity,
                providerKey = it.providerKey,
                providerUid = it.providerUid,
                providerName = it.providerName,
                providerPicture = it.providerPicture,
            )
        }
        userEntity.providers.addAll(providerEntities)
        return userEntity
    }

    fun toDomain(entity: UserEntity): User {
        return User.retrieve(
            userId = entity.userId,
            email = entity.email,
            password = entity.password,
            name = entity.name,
            createdAt = entity.createdAt,
            providers = entity.providers
                .map { ProviderMapper.toDomain(it) }
                .toSet()
        )
    }
}
