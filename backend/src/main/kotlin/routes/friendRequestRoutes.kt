package routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import domain.repository.FriendRequestRepository

fun Route.friendRequestRoute() {
    route("friend-request") {

        authenticate("jwt-auth") {

            get("all") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class).toString()
                val response = FriendRequestRepository.friendRequests(email)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            post("add") {
                val toEmail = call.queryParameters["toEmail"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Please enter email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class).toString()
                val response = FriendRequestRepository.sendFriendRequest(email, toEmail)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            post("accept") {
                val toEmail = call.queryParameters["toEmail"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Please enter email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class).toString()
                val response = FriendRequestRepository.acceptFriendRequest(email, toEmail)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            post("reject") {
                val toEmail = call.queryParameters["toEmail"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Please enter email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class).toString()
                val response = FriendRequestRepository.rejectFriendRequest(email, toEmail)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }
        }
    }

}