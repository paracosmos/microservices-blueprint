package com.matoo.core.port

import com.matoo.core.constant.IdType

interface QueryPort<T> {
    fun findById(id: IdType): T?
}
