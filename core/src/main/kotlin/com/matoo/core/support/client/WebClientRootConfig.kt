package com.matoo.core.support.client

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientRootConfig {

    @Bean
    @Qualifier("externalWebClientBuilder")
    fun externalWebClientBuilder(
        customizers: ObjectProvider<WebClientCustomizer>
    ): WebClient.Builder {
        val builder = WebClient.builder()
        customizers.orderedStream().forEach { it.customize(builder) }
        return builder
    }

    @Bean
    @Primary
    @LoadBalanced
    fun internalWebClientBuilder(
        customizers: ObjectProvider<WebClientCustomizer>
    ): WebClient.Builder {
        val builder = WebClient.builder()
        customizers.orderedStream().forEach { it.customize(builder) }
        return builder
    }
}
