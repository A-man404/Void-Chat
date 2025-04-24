package com.example.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import voidchat.composeapp.generated.resources.Res
import voidchat.composeapp.generated.resources.favicon

@Composable
fun SplashScreen() {
    val backgroundColor = Color(0xFF1E1E2E)
    val accentColor = Color(0xFFBCAEFF)

    var showText by remember { mutableStateOf(false) }

    val scale = remember { Animatable(0f) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = EaseOutBack)
        )
        delay(400)
        showText = true
        delay(1500)

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.favicon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale.value)
            )

            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { it / 2 },
                exit = fadeOut()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Pastel App",
                    fontSize = 22.sp,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
