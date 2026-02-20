package com.matoo.user.adapter.out.persistence.provider

import com.matoo.user.adapter.out.persistence.user.UserEntity
import com.matoo.user.domain.model.provider.Provider
import org.springframework.stereotype.Component

@Component
object ProviderMapper {
    fun toEntity(provider: Provider, userEntity: UserEntity): ProviderEntity {
        return ProviderEntity(
            providerId = provider.providerId,
            user = userEntity,
            providerKey = provider.providerKey,
            providerUid = provider.providerUid,
            providerName = provider.providerName,
            providerPicture = provider.providerPicture,
        )
    }

    fun toDomain(entity: ProviderEntity): Provider {
        return Provider(
            providerId = entity.providerId,
            providerKey = entity.providerKey,
            providerUid = entity.providerUid,
            providerName = entity.providerName,
            providerPicture = entity.providerPicture,
        )
    }
}
