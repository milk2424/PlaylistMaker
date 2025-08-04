package com.example.playlistmaker.domain.interactor

interface ThemeInteractor {

    fun getCurrentTheme(): Boolean

    fun switchTheme(isDark: Boolean)
}