package com.matoo.board.adapter.out.persistence.post

import com.matoo.core.constant.IdType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface PostJpaRepository : JpaRepository<PostEntity, IdType> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update PostEntity p
           set p.deletedAt = :now               
         where p.postId = :id
           and p.deletedAt is null
        """
    )
    fun softDeleteById(
        @Param("id") id: IdType,
        @Param("now") now: Instant = Instant.now()
    ): Int

}
