package com.matoo.user.adapter.out.persistence.user

import com.matoo.core.constant.IdType
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface UserJpaRepository : JpaRepository<UserEntity, IdType> {

    @EntityGraph(attributePaths = ["providers"])
    fun findByEmail(email: String): UserEntity?

    @EntityGraph(attributePaths = ["providers"])
    fun findByUserId(userId: String): UserEntity?

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update UserEntity u
           set u.deletedAt = :now
         where u.userId = :id
           and u.deletedAt is null
        """
    )
    fun softDeleteById(
        @Param("id") id: IdType,
        @Param("now") now: Instant = Instant.now()
    ): Int

    @Query(
        value = """
           select exists(
            select 1
            from users
            where email = :email
        )
    """,
        nativeQuery = true
    )
    fun existsByEmail(@Param("email") email: String): Boolean

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
        update users
           set deleted_at = null
         where email = :email
        """,
        nativeQuery = true
    )
    fun restoreByEmail(
        @Param("email") email: String,
        @Param("now") now: Instant = Instant.now()
    ): Int

}
