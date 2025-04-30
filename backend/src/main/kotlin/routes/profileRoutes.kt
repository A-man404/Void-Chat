package routes

import repository.FriendRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ChangePasswordRequest
import repository.ProfileRepository
import repository.UserRepository

fun Route.profileRoutes() {

    route("/profile") {
        authenticate("jwt-auth") {

            get("fetch") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)
                val response = ProfileRepository.getUserProfile(email.toString())
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            delete("/delete") {
                val password = call.receive<String>()
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)
                val response = ProfileRepository.deleteUser(email.toString(), password)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            post("/update-password") {
                val data = call.receive<ChangePasswordRequest>()
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)
                val response = UserRepository.changePassword(email.toString(), data)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            }

            get("/search/{email}") {
                val email = call.request.queryParameters["email"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing email"
                )
                val res = ProfileRepository.searchUser(email)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            get("/find") {
                val friendEmail = call.queryParameters["friendEmail"] ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Please Provide your friend email"
                )
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class) ?: return@get call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error Occurred"
                )
                val response = FriendRepository.findUser(email,friendEmail)

                call.respond(HttpStatusCode.fromValue(response.statusCode), response)

            }

        }
    }
}