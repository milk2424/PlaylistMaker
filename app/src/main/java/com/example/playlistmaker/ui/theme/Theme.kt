package com.example.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = White,
    background = MainDark,
    secondaryContainer = White,
    surface = MainDark,
    inverseSurface = White,
    inversePrimary = Color(0x40FFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = MainDark,
    background = White,
    secondaryContainer = LightGray,
    surface = Gray,
    inverseSurface = Gray,
    inversePrimary = Color(0x401A1B22)
)

@Composable
fun ColorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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