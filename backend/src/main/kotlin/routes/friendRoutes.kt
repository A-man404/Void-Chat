package routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import repository.FriendRepository

fun Route.friendRoutes() {

    route("/friend") {
        authenticate("jwt-auth") {

            post("/add") {
                val friendEmail = call.queryParameters["friendEmail"] ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Please Provide your friend email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class) ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error Occurred"
                )
                val response = FriendRepository.addFriend(email, friendEmail)

                call.respond(HttpStatusCode.fromValue(response.statusCode), response)

            }

            post("/remove") {

                val friendEmail = call.queryParameters["friendEmail"] ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Please Provide your friend email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class) ?: return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error Occurred"
                )
                val response = FriendRepository.removeFriend(email, friendEmail)

                call.respond(HttpStatusCode.fromValue(response.statusCode), response)

            }

            get("/getFriend") {

                val friendEmail = call.queryParameters["friendEmail"] ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Please Provide your friend email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class) ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error Occurred"
                )
                val response = FriendRepository.getFriend(email, friendEmail)

                call.respond(HttpStatusCode.fromValue(response.statusCode), response)

            }

            get("/getAllFriends") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class) ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error Occurred"
                )
                val response = FriendRepository.getAllFriend(email)

                call.respond(HttpStatusCode.fromValue(response.statusCode), response)

            }
        }
    }
}