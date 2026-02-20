package com.matoo.core.filter

import com.matoo.core.constant.TraceConstants
import org.slf4j.MDC
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ReactiveContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        val userId = request.headers.getFirst(TraceConstants.HEADER_USER_ID) ?: TraceConstants.HEADER_UNKNOWN

        val mutatedRequest = exchange.request.mutate()
            .headers { headers ->
                headers.set(TraceConstants.HEADER_USER_ID, userId)
            }.build()

        val mutatedExchange = exchange.mutate().request(mutatedRequest).build()

        MDC.put(TraceConstants.CTX_USER_ID, userId)

        return chain.filter(mutatedExchange).contextWrite { ctx ->
            ctx.put(TraceConstants.CTX_USER_ID, userId)
        }.doFinally { MDC.clear() }
    }
}
