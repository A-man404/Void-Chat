package com.example.utils

expect fun getDeviceName(): String


object Constants {
    val BASE_URL: String = when (getDeviceName()) {
        "iOS" -> "http://127.0.0.1:8081"
        "Android" -> "http://10.0.2.2:8081"
        else -> "http://localhost:8081"
    }
    val BASE_URL_CHAT: String = when (getDeviceName()) {
        "iOS" -> "127.0.0.1:8081"
        "Android" -> "10.0.2.2:8081"
        else -> "http://localhost:8081"
    }
}
