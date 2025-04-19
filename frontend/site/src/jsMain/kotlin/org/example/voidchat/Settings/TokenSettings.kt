package org.example.voidchat.Settings

import kotlinx.browser.window

object TokenSettings {


    fun saveToken(token: String) {
        window.localStorage.setItem("auth_token", token)
    }


    fun getToken(): String? {
        return window.localStorage.getItem("auth_token")
    }


    fun removeToken() {
        window.localStorage.removeItem("auth_token")
    }
}

