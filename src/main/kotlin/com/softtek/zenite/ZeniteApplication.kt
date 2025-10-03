package com.softtek.zenite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZeniteApplication

fun main(args: Array<String>) {
    runApplication<ZeniteApplication>(*args)
}
