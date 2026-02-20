package com.matoo.gateway.security


import com.matoo.gateway.config.WhitelistConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.*
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.reactive.CorsWebFilter

@Configuration
@EnableWebFluxSecurity
class GatewaySecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val whitelistConfig: WhitelistConfig,
    private val corsConfig: CorsWebFilter
) {

    @Bean
    fun webFluxSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf { it.disable() }.authorizeExchange {
            it.pathMatchers(HttpMethod.OPTIONS).permitAll()
            whitelistConfig.whitelist.forEach { path ->
                it.pathMatchers(path).permitAll()
            }
            it.pathMatchers(HttpMethod.GET).authenticated()
            it.pathMatchers(HttpMethod.POST).authenticated()
            it.pathMatchers(HttpMethod.PATCH).authenticated()
            it.pathMatchers(HttpMethod.DELETE).authenticated()
            it.pathMatchers(HttpMethod.PUT).authenticated()
        }.headers { headerSpec ->
            headerSpec.xssProtection {}
            headerSpec.contentSecurityPolicy {
                it.policyDirectives(
                    """
                        script-src 'self'
                    """.trimIndent()
                )
            }
        }.addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION).addFilterAt(
            corsConfig, SecurityWebFiltersOrder.CORS
        )

        return http.build()
    }
}
