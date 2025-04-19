package org.example.voidchat.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.example.voidchat.Settings.TokenSettings
import org.example.voidchat.client.KtorClient
import org.example.voidchat.model.RepositoryResponse
import org.example.voidchat.model.User
import org.example.voidchat.model.User1

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

    suspend fun loginUser(user: User1): RepositoryResponse<String> {
        return try {
            val response: HttpResponse = client.post("http://0.0.0.0:8081/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val responseBody = response.body<RepositoryResponse<String>>()
            if (responseBody.statusCode == 200 && responseBody.data != null) {
                TokenSettings.saveToken(responseBody.data)
                RepositoryResponse(
                    data = responseBody.data,
                    message = "Login successful",
                    statusCode = 200
                )
            } else {
                responseBody
            }
        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = e.message ?: "Unknown error",
                statusCode = 500
            )
        }

    }
}


