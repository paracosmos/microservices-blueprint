package com.matoo.gateway.security

import com.matoo.core.constant.TraceConstants
import com.matoo.core.util.JwtUtil
import com.matoo.gateway.config.WhitelistConfig
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.util.*
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val whitelistConfig: WhitelistConfig,
    @param:Value("\${jwt.access.public-key}") private val accessPublicKey: String
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.uri.path
        val matcher = AntPathMatcher()

        if (exchange.request.method == HttpMethod.OPTIONS) return chain.filter(exchange)

        whitelistConfig.whitelist.forEach {
            if (matcher.match(it, path)) {
                return chain.filter(exchange)
            }
        }

        return mono {
            val securityContext = extractSecurityContext(exchange)
            if (securityContext != null) {
                val userId = securityContext.authentication.name
                val requestId = exchange.request.headers.getFirst(TraceConstants.HEADER_REQUEST_ID)
                    ?: UUID.randomUUID().toString()

                val modifiedExchange = exchange.mutate()
                    .request { builder ->
                        builder.headers { headers ->
                            headers.set(TraceConstants.HEADER_USER_ID, userId)
                            headers.set(TraceConstants.HEADER_REQUEST_ID, requestId)
                        }
                    }.build()

                chain.filter(modifiedExchange)
                    .contextWrite { ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)) }
                    .contextWrite { ctx ->
                        ctx
                            .put(TraceConstants.CTX_REQUEST_ID, requestId)
                            .put(TraceConstants.CTX_USER_ID, userId)
                    }
                    .awaitSingleOrNull()
            } else {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                exchange.response.setComplete().awaitSingleOrNull()
            }
        }
    }

    private suspend fun extractSecurityContext(exchange: ServerWebExchange): SecurityContextImpl? {
        val request = exchange.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (authHeader.isNullOrBlank() || !authHeader.startsWith(TraceConstants.HEADER_PREFIX_BEARER)) return null

        val token = authHeader.removePrefix(TraceConstants.HEADER_PREFIX_BEARER).trim()
        if (!JwtUtil.validateToken(token, accessPublicKey)) return null

        val userId = JwtUtil.getClaims(token, accessPublicKey).subject
        val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())

        return SecurityContextImpl(authentication)
    }
}
