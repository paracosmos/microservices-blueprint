package com.matoo.core.constant

enum class RemoteService(val baseUrl: String) {
    AUTH("lb://auth"),
    USER("lb://user"),
    BOARD("lb://board")
}
