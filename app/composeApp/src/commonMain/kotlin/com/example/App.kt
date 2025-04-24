package com.example


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.pages.LoginScreen
import com.example.pages.SignUpScreen
import com.example.pages.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        LoginScreen()
//SplashScreen()
//        SignUpScreen()
    }
}