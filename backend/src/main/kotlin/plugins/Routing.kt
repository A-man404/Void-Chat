package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.AdminRoutes
import routes.AuthRoutes
import routes.chatRoutes
import routes.ProfileRoutes

fun Application.configureRouting(jwtConfig: JWTConfig) {

    routing {
        chatRoutes()
        AdminRoutes()
        AuthRoutes(jwtConfig)
        ProfileRoutes()

    }
}
