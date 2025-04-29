package routes

import com.example.plugins.JWTConfig
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LoginRequest
import model.RegisterRequest
import domain.repository.UserRepository

fun Route.authRoutes(jwtConfig: JWTConfig) {


    route("/auth") {
        post("/register") {
            val user = call.receive<RegisterRequest>()
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

