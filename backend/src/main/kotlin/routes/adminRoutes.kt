package routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import model.ChangeRoleRequest
import model.RepositoryResponse
import model.UserRole
import domain.repository.AdminRepository

fun Route.adminRoutes() {

    route("/admin") {
        authenticate("jwt-auth") {

            get("/users") {

                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim("role", UserRole::class) ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Error Occurred"
                )

                if (role == UserRole.ADMIN) {
                    val res = AdminRepository.getAllUsers(page = page, limit = limit)
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)
                } else {
                    call.respond(
                        message = "You don't have access to view this operation", status = HttpStatusCode.Unauthorized
                    )
                }

            }

            get("/user/{email}") {

                val email = call.pathParameters["email"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Please enter an email"
                )

                val principal = call.principal<JWTPrincipal>()

                val role = principal?.getClaim("role", UserRole::class) ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Error Occurred"
                )

                if (role == UserRole.ADMIN) {
                    val res = AdminRepository.getUserByEmail(email)
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)
                } else {
                    call.respond(
                        message = "You don't have access to view this operation", status = HttpStatusCode.Unauthorized
                    )
                }
            }

            post("/change-role") {
                val res = call.receive<ChangeRoleRequest>()
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim("role", UserRole::class) ?: return@post call.respond(
                    RepositoryResponse(
                        data = false,
                        message = "Role not found in token",
                        statusCode = HttpStatusCode.Unauthorized.value
                    )
                )
                val response = AdminRepository.changeRoleRequest(role, res)
                call.respond(message = response, status = HttpStatusCode.fromValue(response.statusCode))
            }

            post("/deactivate/{email}") {
                val email = call.pathParameters["email"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Please enter an email"
                )
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim("role", UserRole::class) ?: return@post call.respond(
                    RepositoryResponse(
                        data = false,
                        message = "Role not found in token",
                        statusCode = HttpStatusCode.Unauthorized.value
                    )
                )
                if (role == UserRole.ADMIN) {
                    val res = AdminRepository.deactivateUser(email)
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)
                } else {
                    call.respond(
                        message = "You don't have access to view this operation", status = HttpStatusCode.Unauthorized
                    )
                }
            }

            post("/activate/{email}") {
                val email = call.pathParameters["email"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Please enter an email"
                )
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim("role", UserRole::class) ?: return@post call.respond(
                    RepositoryResponse(
                        data = false,
                        message = "Role not found in token",
                        statusCode = HttpStatusCode.Unauthorized.value
                    )
                )
                if (role == UserRole.ADMIN) {
                    val res = AdminRepository.activateUser(email)
                    call.respond(HttpStatusCode.fromValue(res.statusCode), res)
                } else {
                    call.respond(
                        message = "You don't have access to view this operation", status = HttpStatusCode.Unauthorized
                    )
                }
            }
        }
    }

}