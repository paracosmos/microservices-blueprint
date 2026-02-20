package com.matoo.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.matoo.core.util.CoreUtil
import com.matoo.user.infrastructure.config.UserModuleConfig
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import

@SpringBootApplication
@EnableDiscoveryClient
@Import(UserModuleConfig::class)
class UserApplication

fun main(args: Array<String>) {
    CoreUtil.initEnv()
    runApplication<UserApplication>(*args)
}
