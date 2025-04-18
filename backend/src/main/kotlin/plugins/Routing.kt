package com.example.plugins

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import plugins.JWTConfig
import routes.AuthRoutes

fun Application.configureRouting(jwtConfig: JWTConfig) {

    routing {
        AuthRoutes(jwtConfig)
    }
}
