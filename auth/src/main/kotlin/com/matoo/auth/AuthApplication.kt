package com.matoo.auth

import com.matoo.core.util.CoreUtil
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
    CoreUtil.initEnv()
    runApplication<AuthApplication>(*args)
}
