package com.example.vectors.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6B4EFF),
    secondary = Color(0xFFEC4899),
    background = Color(0xFFF8F7FF),
    surface = Color.White,
)

@Composable
fun VectorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}