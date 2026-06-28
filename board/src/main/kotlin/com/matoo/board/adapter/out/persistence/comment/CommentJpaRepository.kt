package com.matoo.board.adapter.out.persistence.comment

import com.matoo.core.constant.IdType
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface CommentJpaRepository : JpaRepository<CommentEntity, IdType>{

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update CommentEntity c
           set c.deletedAt = :now               
         where c.postId = :id
           and c.deletedAt is null
        """
    )
    fun softDeleteById(
        @Param("id") id: IdType,
        @Param("now") now: Instant = Instant.now()
    ): Int

}
