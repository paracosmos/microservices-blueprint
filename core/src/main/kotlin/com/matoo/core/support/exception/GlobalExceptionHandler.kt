package com.matoo.core.support.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException

@ControllerAdvice
class GlobalExceptionHandler(private val objectMapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(e: ServerWebInputException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatus(e.statusCode)
        return ResponseEntity.badRequest().body(problemDetail)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(e: WebExchangeBindException): ResponseEntity<ProblemDetail> {
        val error = e.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: HttpStatus.BAD_REQUEST.reasonPhrase)
        }
        val problemDetail = ProblemDetail.forStatus(e.statusCode).apply {
            setProperty("error", error)
        }
        return ResponseEntity.badRequest().body(problemDetail)
    }

    @ExceptionHandler(WebClientRequestException::class)
    fun handleWebClientRequestException(e: WebClientRequestException): ResponseEntity<ProblemDetail> {
        logger.error("handleWebClientRequestException", e)
        val problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problemDetail)
    }

    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientResponseException(e: WebClientResponseException): ResponseEntity<ProblemDetail> {
        logger.error("handleWebClientResponseException", e)
        val upstreamStatus = e.statusCode
        val httpStatusEnum: HttpStatus? = HttpStatus.resolve(upstreamStatus.value())
        val fallbackError = httpStatusEnum?.reasonPhrase ?: HttpStatus.BAD_GATEWAY.reasonPhrase
        val content = e.responseBodyAsString
        val error = runCatching {
            val node = objectMapper.readTree(content)
            when {
                node.has("error") -> node["error"].asText()
                node.has("detail") -> node["detail"].asText()
                node.has("reason") -> node["reason"].asText()
                node.has("message") -> node["message"].asText()
                else -> fallbackError
            }
        }.getOrElse { fallbackError }
        val problemDetail = ProblemDetail.forStatus(upstreamStatus).apply {
            setProperty("error", error)
        }
        return ResponseEntity.status(upstreamStatus).body(problemDetail)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ProblemDetail {
        val status = e.statusCode
        return ProblemDetail.forStatusAndDetail(status, e.reason ?: status.toString())
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ProblemDetail> {
        // require(...) 등 입력 검증 실패는 클라이언트 잘못이므로 500이 아닌 400으로 매핑한다.
        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            e.message ?: HttpStatus.BAD_REQUEST.reasonPhrase
        )
        return ResponseEntity.badRequest().body(problemDetail)
    }
}
