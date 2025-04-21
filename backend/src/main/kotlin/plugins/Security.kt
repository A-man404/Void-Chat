package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import model.UserRole
import java.util.*

fun Application.configureSecurity(config: JWTConfig) {
    install(Authentication) {
        jwt("jwt-auth") {
            realm = config.realm

            val jwtVerifier =
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()

            verifier(jwtVerifier)

            validate { jwtCredential ->
                val email = jwtCredential.payload.getClaim("email").asString()
                if (!email.isNullOrBlank()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respondText("Token is not valid or has expired", status = HttpStatusCode.Unauthorized)

            }
        }
    }
}

fun generateToken(config: JWTConfig, email: String, role: UserRole): String {
    return JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("email", email)
        .withClaim("role", role.name)
        .withExpiresAt(Date(System.currentTimeMillis() + config.tokenExpiry))
        .sign(Algorithm.HMAC256(config.secret))
}


data class JWTConfig(
    val realm: String,
    val secret: String,
    val issuer: String,
    val audience: String,
    val tokenExpiry: Long
)
