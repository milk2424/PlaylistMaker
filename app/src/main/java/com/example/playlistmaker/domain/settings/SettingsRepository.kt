package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsRepository {
    fun getCurrentTheme(): ThemeSettings

    fun switchTheme(themeSettings: ThemeSettings)
}