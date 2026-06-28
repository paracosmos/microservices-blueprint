package com.matoo.board.adapter.out.persistence.file

import com.matoo.core.constant.IdType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface FileJpaRepository : JpaRepository<FileEntity, IdType>{
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update FileEntity f
           set f.deletedAt = :now               
         where f.fileId = :id
           and f.deletedAt is null
        """
    )
    fun softDeleteById(
        @Param("id") id: IdType,
        @Param("now") now: Instant = Instant.now()
    ): Int
}
