package com.matoo.board.application.model

import com.matoo.board.domain.model.File

data class FileInfo(
    val fileId: String,
    val userId: String?,
    val storageKey: String,
    val url: String?,
    val fileName: String?,
    val mimeType: String?,
    val fileSize: Long?
)

fun File.toFileInfo(): FileInfo {
    return FileInfo(
        fileId = fileId,
        userId = userId,
        storageKey = storageKey,
        url = null,
        fileName = fileName,
        mimeType = mimeType,
        fileSize = fileSize,
    )
}
