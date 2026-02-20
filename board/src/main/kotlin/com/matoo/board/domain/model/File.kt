package com.matoo.board.domain.model

import java.time.Instant

data class File(
    val fileId: String,
    val userId: String? = null,
    val relatedId: String? = null,
    val relatedType: RelatedType? = null,

    val storageKey: String,
    val storage: StorageProvider = StorageProvider.S3,

    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,

    val status: FileStatus = FileStatus.PENDING,

    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
