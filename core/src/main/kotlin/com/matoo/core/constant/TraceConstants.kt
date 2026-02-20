package com.matoo.core.constant

object TraceConstants {
    const val HEADER_USER_ID = "X-User-Id"

    const val HEADER_REQUEST_ID = "X-Request-Id"
    const val HEADER_ENC_ID = "X-Enc-Id"
    const val HEADER_FORWARDED_FOR = "X-Forwarded-For"
    const val HEADER_USER_AGENT = "User-Agent"
    const val HEADER_UNKNOWN = "Unknown"
    const val HEADER_PREFIX_BEARER = "Bearer "
    const val CTX_REQUEST_ID = "requestId"
    const val CTX_USER_ID = "userId"
    const val SEND_DELAY_MS = 500L
    const val READ_OFFSET = "0-0"
    const val SYSTEM = "SYSTEM"
    const val ARGS2 = "{} - {}"

    const val ACCESS_TOKEN_TTL = 60 * 60 * 1000L // 1 hour
    const val REFRESH_TOKEN_TTL = 30 * 60 * 60 * 24 * 1000L // 30 days

    const val TIMEOUT_SECOND = 30L

    const val TTL_MINUTES = 1L

}
