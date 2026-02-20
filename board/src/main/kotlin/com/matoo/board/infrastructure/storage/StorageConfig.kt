package com.matoo.board.infrastructure.storage

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(StorageProperties::class)
class StorageConfig
