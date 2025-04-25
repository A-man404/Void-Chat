package com.example.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.ui.theme.primaryPastel

@Composable
fun LoadingBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "Loading Bar")
    val progress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ), label = "progress animation"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .graphicsLayer {
                scaleX = progress.value
                scaleY = progress.value
                alpha = 1 - progress.value
            }
            .border(5.dp, color = primaryPastel, shape = CircleShape))
}