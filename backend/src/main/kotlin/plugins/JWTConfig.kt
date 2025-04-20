package plugins

import com.example.plugins.JWTConfig
import io.ktor.server.application.*

fun Application.JWTConfig(): JWTConfig {


    val jwt = environment.config.config("ktor.jwt")
    val realm = jwt.property("realm").getString()
    val issuer = jwt.property("issuer").getString()
    val secret = jwt.property("secret").getString()
    val audience = jwt.property("audience").getString()
    val tokenExpiry = jwt.property("expiry").getString()


    return JWTConfig(
        realm = realm,
        secret = secret,
        issuer = issuer,
        audience = audience,
        tokenExpiry = tokenExpiry.toLong()
    )
}