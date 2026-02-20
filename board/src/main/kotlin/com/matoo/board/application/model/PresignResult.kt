package com.matoo.board.application.model

data class PresignResult(
    val uploadUrl: String,
    val fileId: String,
    val expiresInSec: Long?,
    val url: String
)
