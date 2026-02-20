package com.matoo.board.adapter.out.persistence.file

import com.matoo.board.domain.model.File
import org.springframework.stereotype.Component

@Component
class FileMapper {

    fun toEntity(file: File): FileEntity {
        return FileEntity(
            fileId = file.fileId,
            userId = file.userId,
            relatedId = file.relatedId,
            relatedType = file.relatedType,
            storageKey = file.storageKey,
            storage = file.storage,
            fileName = file.fileName,
            mimeType = file.mimeType,
            fileSize = file.fileSize,
            status = file.status,
            createdAt = file.createdAt,
            updatedAt = file.updatedAt,
        )
    }

    fun toDomain(fileEntity: FileEntity): File {
        return File(
            fileId = fileEntity.fileId,
            userId = fileEntity.userId,
            relatedId = fileEntity.relatedId,
            relatedType = fileEntity.relatedType,
            storageKey = fileEntity.storageKey,
            storage = fileEntity.storage,
            fileName = fileEntity.fileName,
            mimeType = fileEntity.mimeType,
            fileSize = fileEntity.fileSize,
            status = fileEntity.status,
            createdAt = fileEntity.createdAt,
            updatedAt = fileEntity.updatedAt
        )
    }
}
