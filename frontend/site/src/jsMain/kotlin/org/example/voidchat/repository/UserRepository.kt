package org.example.voidchat.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.example.voidchat.client.KtorClient
import org.example.voidchat.model.RepositoryResponse
import org.example.voidchat.model.User

object UserRepository {
    val client = KtorClient.client

    suspend fun registerUser(user: User): RepositoryResponse<Boolean> {

        return try {

            val response: HttpResponse = client.post("http://0.0.0.0:8081/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            response.body<RepositoryResponse<Boolean>>()
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = e.message ?: "Unknown error",
                statusCode = 500
            )
        }

    }
}


