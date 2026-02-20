package com.matoo.board.application.port.out

import com.matoo.core.constant.IdType

interface PostCachePort {
    fun evict(id: IdType)
    fun evictAll() = Unit
}
