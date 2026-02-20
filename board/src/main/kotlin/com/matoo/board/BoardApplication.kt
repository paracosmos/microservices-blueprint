package com.matoo.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.matoo.core.util.CoreUtil
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
class BoardApplication

fun main(args: Array<String>) {
    CoreUtil.initEnv()
    runApplication<BoardApplication>(*args)
}
