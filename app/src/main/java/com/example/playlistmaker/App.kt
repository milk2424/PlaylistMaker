package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
    }

    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
        const val THEME_KEY = "THEME_KEY"
        lateinit var instance: App
            private set
    }
}