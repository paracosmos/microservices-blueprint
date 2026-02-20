package com.matoo.auth.infrastructure.config.client

import com.matoo.core.support.client.TraceWebClientFilter
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.propagation.Propagator
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnBean(Tracer::class)
class WebClientTracingConfig {
    @Bean
    fun tracingWebClientCustomizer(
        tracer: Tracer,
        propagator: Propagator
    ): WebClientCustomizer =
        WebClientCustomizer { builder ->
            builder.filter(TraceWebClientFilter(tracer, propagator))
        }
}
