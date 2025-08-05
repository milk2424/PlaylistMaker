package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.App.Companion.THEME_KEY
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {
    override fun getCurrentTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun switchTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, isDark).apply()
    }
}