package com.example.repository

import com.example.model.LoginRequest
import com.example.model.RepositoryResponse
import com.example.network.ktorClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object AuthRepository {


    suspend fun userLogin(email: String, password: String) {

        val response = ktorClient.post("http://0.0.0.0:8081/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

        val repositoryResponse: RepositoryResponse<String> = response.body()
        if (repositoryResponse.statusCode == 200) {
            println("User Login")
        } else {
            println(repositoryResponse.message)
        }
    }

}