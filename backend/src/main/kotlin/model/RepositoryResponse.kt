package model

import kotlinx.serialization.Serializable

@Serializable
data class RepositoryResponse<T>(
    val data: T? = null,
    val message: String,
    val statusCode: Int
)
