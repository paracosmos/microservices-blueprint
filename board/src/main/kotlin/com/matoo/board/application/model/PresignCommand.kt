package com.matoo.board.application.model

data class PresignCommand(
    val fileName: String,
    val contentType: String,
    val size: Long
)
