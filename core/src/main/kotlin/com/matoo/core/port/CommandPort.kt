package com.matoo.core.port

interface CommandPort<T> {
    fun save(data: T): T
}
