package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getCurrentTheme(): ThemeSettings {
        return settingsRepository.getCurrentTheme()
    }

    override fun switchTheme(themeSettings: ThemeSettings) {
        settingsRepository.switchTheme(themeSettings)
    }
}