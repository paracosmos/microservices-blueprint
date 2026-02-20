package com.matoo.gateway

import com.matoo.core.util.CoreUtil
import com.matoo.gateway.config.GatewayModuleConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import

@SpringBootApplication
@EnableDiscoveryClient
@Import(GatewayModuleConfig::class)
class GatewayApplication

fun main(args: Array<String>) {
    CoreUtil.initEnv()
    runApplication<GatewayApplication>(*args)
}
