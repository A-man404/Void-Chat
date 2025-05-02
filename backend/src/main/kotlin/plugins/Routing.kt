package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.*

fun Application.configureRouting(jwtConfig: JWTConfig) {

    routing {
        chatRoutes()
        authRoutes(jwtConfig)
        adminRoutes()
        profileRoutes()
        friendRoutes()
        friendRequestRoute()
        groupRoutes()

    }
}
