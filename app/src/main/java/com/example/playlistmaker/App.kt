package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Creator.provideThemeInteractor().let {
            val isDark = it.getCurrentTheme()
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
            it.switchTheme(isDark)
        }

    }
    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
        const val THEME_KEY = "THEME_KEY"
        lateinit var instance: App
            private set
    }
}