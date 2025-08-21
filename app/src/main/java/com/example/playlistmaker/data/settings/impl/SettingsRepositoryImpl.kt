package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App.Companion.THEME_KEY
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    override fun getCurrentTheme(): ThemeSettings {
        val isNight = sharedPreferences.getBoolean(THEME_KEY, false)
        return ThemeSettings(isNight)
    }

    override fun switchTheme(themeSettings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, themeSettings.isNight).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (themeSettings.isNight) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}