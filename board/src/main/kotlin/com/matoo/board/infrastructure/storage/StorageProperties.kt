package com.matoo.board.infrastructure.storage

import com.matoo.board.domain.model.StorageProvider
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "storage")
data class StorageProperties(
    val provider: StorageProvider = StorageProvider.S3,
    val publicBaseUrl: String? = null
)
