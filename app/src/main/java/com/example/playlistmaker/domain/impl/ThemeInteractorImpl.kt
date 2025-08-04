package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App.Companion.THEME_KEY
import com.example.playlistmaker.domain.interactor.ThemeInteractor

class ThemeInteractorImpl(private val sharedPreferences: SharedPreferences) : ThemeInteractor {
    override fun getCurrentTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun switchTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(THEME_KEY, isDark).apply()
    }
}