package com.matoo.user.adapter.out.persistence

import com.matoo.user.adapter.out.cache.Key
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KeyRepositoryRedis : CrudRepository<Key, String>
