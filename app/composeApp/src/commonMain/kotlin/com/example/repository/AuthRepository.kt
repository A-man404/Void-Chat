package com.example.repository

import com.example.model.LoginRequest
import com.example.model.RegisterRequest
import com.example.model.RepositoryResponse
import com.example.network.ktorClient
import com.example.utils.Constants
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object AuthRepository {

    suspend fun userLogin(email: String, password: String): Result<RepositoryResponse<String>> {
        return try {
            val response = ktorClient.post("${Constants.BASE_URL}/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            val repositoryResponse: RepositoryResponse<String> = response.body()

            if (repositoryResponse.statusCode == 200) {
                Result.success(repositoryResponse)
            } else {
                Result.failure(Exception("Error: ${repositoryResponse.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<RepositoryResponse<Boolean>> {
        return try {
            val response = ktorClient.post("${Constants.BASE_URL}/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name, email, password))
            }

            val repositoryResponse: RepositoryResponse<Boolean> = response.body()

            if (repositoryResponse.statusCode == 200) {
                Result.success(repositoryResponse)
            } else {
                Result.failure(Exception("Error: ${repositoryResponse.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}