package com.matoo.board.adapter.out.persistence.file

import com.matoo.board.application.port.out.file.FileCommandPort
import com.matoo.board.application.port.out.file.FileQueryPort
import com.matoo.board.domain.model.File
import com.matoo.board.domain.model.FileStatus
import com.matoo.core.constant.IdType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FilePersistenceAdapter(
    private val fileJpaRepository: FileJpaRepository,
    private val fileMapper: FileMapper
) : FileCommandPort, FileQueryPort {

    @Transactional
    override fun save(data: File): File {
        val entity = fileMapper.toEntity(data)
        val saved = fileJpaRepository.save(entity)
        return fileMapper.toDomain(saved)
    }

    override fun deleteById(id: IdType) {
        fileJpaRepository.softDeleteById(id)
    }

    @Transactional
    override fun markUploaded(fileId: String, userId: String): Boolean {
        val entity = fileJpaRepository.findByIdOrNull(fileId) ?: return false
        entity.status = FileStatus.ACTIVE
        fileJpaRepository.save(entity)
        return true
    }

    override fun findById(id: IdType): File? {
        val file = fileJpaRepository.findByIdOrNull(id)?.let { fileMapper.toDomain(it) }
        return file
    }
}
