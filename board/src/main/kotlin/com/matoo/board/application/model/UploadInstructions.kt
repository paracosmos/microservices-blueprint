package com.matoo.board.application.model

import com.matoo.board.domain.model.StorageProvider

data class UploadInstructions(
    val provider: StorageProvider,
    val method: String,
    val uploadUrl: String,
    val headers: Map<String, String> = emptyMap(),
    val token: String? = null,
    val key: String,
    val expiresInSec: Long? = null,
    val url: String
)
