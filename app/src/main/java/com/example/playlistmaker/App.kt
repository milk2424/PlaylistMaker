package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Creator.provideSettingsInteractor().let {
            val isDark = it.getCurrentTheme()
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