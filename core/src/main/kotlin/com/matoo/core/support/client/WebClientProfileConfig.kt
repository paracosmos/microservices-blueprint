package com.matoo.core.support.client

import com.matoo.core.constant.TraceConstants
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientProfileConfig() {

    @Bean
    @Profile("local", "test")
    fun insecureClientCustomizer(): WebClientCustomizer = WebClientCustomizer { builder ->
        val sslContext = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        val httpClient = HttpClient.create()
            .secure { it.sslContext(sslContext) }
            .responseTimeout(Duration.ofSeconds(TraceConstants.TIMEOUT_SECOND))
        builder.clientConnector(ReactorClientHttpConnector(httpClient))
    }
}
