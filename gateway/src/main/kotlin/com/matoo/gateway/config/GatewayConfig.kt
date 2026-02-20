package com.matoo.gateway.config

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GatewayConfig {
    @Bean
    fun discoveryClientRouteDefinitionLocator(
        discoveryClient: ReactiveDiscoveryClient,
        properties: DiscoveryLocatorProperties
    ): DiscoveryClientRouteDefinitionLocator {
        return DiscoveryClientRouteDefinitionLocator(discoveryClient, properties)
    }
}
