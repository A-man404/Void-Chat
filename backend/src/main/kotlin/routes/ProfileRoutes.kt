package routes

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import repository.ProfileRepository

fun Route.ProfileRoutes() {

    route("/profile") {
        authenticate("jwt-auth") {
            get("fetch") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)

                val response = ProfileRepository.getUserProfile(email.toString())
                call.respond(response)
            }

            delete("/delete") {
                val password = call.receive<String>()
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)
                val response = ProfileRepository.deleteUser(email.toString(), password)
                call.respond(response)
            }

        }
    }
}