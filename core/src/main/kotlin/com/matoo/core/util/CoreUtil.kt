package com.matoo.core.util

import com.github.f4b6a3.ulid.UlidCreator
import io.github.cdimascio.dotenv.dotenv

object CoreUtil {
    private val dotenv = dotenv { ignoreIfMissing = true }

    fun generateId(): String {
        return UlidCreator.getUlid().toString()
    }

    fun initEnv() {
        dotenv.entries().forEach { entry ->
            System.setProperty(entry.key, entry.value)
        }
    }
}
