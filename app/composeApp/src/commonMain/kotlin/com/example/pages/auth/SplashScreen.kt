package com.example.pages.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.pages.main.MainScreen
import com.example.ui.theme.backgroundColor
import com.example.ui.theme.primaryPastel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import voidchat.composeapp.generated.resources.Res
import voidchat.composeapp.generated.resources.favicon

object SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isVisible by remember { mutableStateOf(false) }
        var isTextVisible by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (isVisible) 1f else 0.5f,
            animationSpec = tween(durationMillis = 800, easing = EaseOutBack)
        )

        val textOffset by animateFloatAsState(
            targetValue = if (isTextVisible) 0f else 20f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        LaunchedEffect(Unit) {
            try {
                delay(500)
                isVisible = true
                delay(1000)
                isTextVisible = true
                delay(1500)

                navigator.replace(MainScreen)
            } catch (e: Throwable) {
                println("Failed to navigate: ${e.message}")
            }
        }


        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Image(
                painterResource(Res.drawable.favicon),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
            )

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(visible = isTextVisible) {
                Text(
                    "Void Chat",

                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = primaryPastel
                    ),
                    modifier = Modifier.offset(y = textOffset.dp)
                )
            }
        }
    }
}