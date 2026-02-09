package com.example.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = White,
    background = MainDark,
    secondaryContainer = White,
    surface = MainDark,
    inverseSurface = White,
)

private val LightColorScheme = lightColorScheme(
    primary = MainDark,
    background = White,
    secondaryContainer = LightGray,
    surface = Gray,
    inverseSurface = Gray,
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