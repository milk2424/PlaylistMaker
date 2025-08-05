package com.example.playlistmaker.domain.repository

interface ThemeRepository {
    fun getCurrentTheme(): Boolean

    fun switchTheme(isDark: Boolean)
}