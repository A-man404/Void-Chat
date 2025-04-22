package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.adminRoutes
import routes.authRoutes
import routes.chatRoutes
import routes.profileRoutes

fun Application.configureRouting(jwtConfig: JWTConfig) {

    routing {
        chatRoutes()
        adminRoutes()
        authRoutes(jwtConfig)
        profileRoutes()

    }
}
