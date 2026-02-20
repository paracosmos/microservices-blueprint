package com.matoo.gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class CorsConfig(
    @param:Value("\${cors.origins}") private val corsOrigins: Array<String>
) : WebFluxConfigurer {

    init {
        println(corsOrigins)
    }

    val allowedMethods = arrayOf(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name(),
        HttpMethod.PATCH.name(),
        HttpMethod.OPTIONS.name()
    )

    val allowedHeaders = arrayOf(
        HttpHeaders.CONTENT_TYPE,
        HttpHeaders.AUTHORIZATION,
        "x-enc-id",
    )

    @Bean
    fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()
        config.allowedOrigins = corsOrigins.toList()
        config.allowedOriginPatterns = corsOrigins.map { "$it/**" }
        config.allowedMethods = allowedMethods.toList()
        config.allowedHeaders = allowedHeaders.toList()
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", config)

        return CorsWebFilter(source)
    }
}
