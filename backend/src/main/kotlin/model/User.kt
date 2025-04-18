package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)


