package com.matoo.core.support.client

import com.matoo.core.constant.TraceConstants
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.propagation.Propagator
import org.slf4j.MDC
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono
import reactor.util.context.ContextView

class TraceWebClientFilter(
    private val tracer: Tracer,
    private val propagator: Propagator
) : ExchangeFilterFunction {

    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        return Mono.deferContextual { contextView: ContextView ->
            val span = tracer.currentSpan() ?: tracer.nextSpan().name("webclient-span").start()
            val builder = ClientRequest.from(request)

            propagator.inject(span.context(), builder) { carrier, key, value ->
                carrier?.header(key, value)
            }

            val contextUserId = contextView.getOrDefault<String>(TraceConstants.CTX_USER_ID, null)
            val userId = contextUserId ?: MDC.get(TraceConstants.CTX_USER_ID)
            userId?.let { builder.header(TraceConstants.HEADER_USER_ID, it) }

            next.exchange(builder.build())
        }
    }
}
