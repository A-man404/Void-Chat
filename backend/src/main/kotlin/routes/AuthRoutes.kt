package routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LoginRequest
import model.User
import plugins.JWTConfig
import repository.UserRepository

fun Route.AuthRoutes(jwtConfig: JWTConfig) {


    route("/auth") {
        post("/register") {
            val user = call.receive<User>()
            val response = UserRepository.registerUser(user)
            call.respond(HttpStatusCode.fromValue(response.statusCode), response)
        }
        post("/login") {
            val user = call.receive<LoginRequest>()
            val response = UserRepository.loginUser(user, jwtConfig)
            call.respond(HttpStatusCode.fromValue(response.statusCode), response)
        }
    }
}

