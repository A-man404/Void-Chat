package com.example.plugins

import io.ktor.server.application.*
import plugins.JWTConfig
import plugins.configureWebsockets
import plugins.installCORS

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    val jwtConfig = JWTConfig()
    installCORS()
    configureWebsockets()
    configureSerialization()
    configureSecurity(jwtConfig)
    configureRouting(jwtConfig)
}
