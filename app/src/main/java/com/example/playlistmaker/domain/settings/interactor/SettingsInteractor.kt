package com.example.playlistmaker.domain.settings.interactor

import com.example.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getCurrentTheme(): ThemeSettings

    fun switchTheme(themeSettings: ThemeSettings)
}