package model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String,
    val createdAt: String = LocalDateTime.now().toString(),
    val profileImage: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
