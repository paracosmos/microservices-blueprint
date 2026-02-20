package com.matoo.user.adapter.out.cache

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Key")
data class Key(
    @Id val id: String,
    val value: String
)
