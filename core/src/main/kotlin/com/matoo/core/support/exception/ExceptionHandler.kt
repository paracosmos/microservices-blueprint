package com.matoo.core.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun <T : Any> T?.orNotFound(message: String? = null): T =
    this ?: throw ResponseStatusException(
        HttpStatus.NOT_FOUND,
        message ?: HttpStatus.NOT_FOUND.reasonPhrase
    )

inline fun requireStatus(
    condition: Boolean,
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    reason: () -> String = { status.reasonPhrase }
) {
    if (!condition) {
        throw ResponseStatusException(status, reason())
    }
}
