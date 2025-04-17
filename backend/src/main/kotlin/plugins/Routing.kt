package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.demoRoutes

fun Application.configureRouting() {


    routing {
        demoRoutes()
    }
}
