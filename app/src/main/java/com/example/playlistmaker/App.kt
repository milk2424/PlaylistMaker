package com.example.playlistmaker

import android.app.Application

class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Creator.provideThemeInteractor().let {
            it.switchTheme(it.getCurrentTheme())
        }

    }

    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
        const val THEME_KEY = "THEME_KEY"
        lateinit var instance: App
            private set
    }
}