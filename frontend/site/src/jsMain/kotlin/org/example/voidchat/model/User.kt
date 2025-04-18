package org.example.voidchat.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class RepositoryResponse<T>(
    val data: T? = null,
    val message: String,
    val statusCode: Int
)
