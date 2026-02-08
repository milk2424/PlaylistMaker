package com.example.playlistmaker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    background = Color(0xFF1A1B22),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    background = Color(0xFFFFFFFF),
)

@Composable
fun ColorTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (!darkTheme) {
            LightColorScheme
        } else {
            DarkColorScheme
        }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}