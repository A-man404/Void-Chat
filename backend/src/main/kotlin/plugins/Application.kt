package com.example.plugins

import io.ktor.server.application.*
import plugins.JWTConfig
import plugins.configureJWTAuthentication

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    val jwtConfig = JWTConfig()
    configureJWTAuthentication(jwtConfig)
    configureSerialization()
    configureSecurity()
    configureRouting(jwtConfig)
}
