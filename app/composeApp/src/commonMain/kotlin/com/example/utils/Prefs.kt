package com.example.utils

import com.russhwolf.settings.Settings

object Prefs {
    private val settings: Settings = Settings()

    fun saveToken(token: String) {
        settings.putString("token", token)
    }

    fun clearToken() {
        settings.remove("token")
    }

    fun getToken(): String? {
        return settings.getStringOrNull("token")
    }
}