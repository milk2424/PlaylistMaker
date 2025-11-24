package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.useCaseModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule, useCaseModule)
        }
        settingsInteractor.switchTheme(settingsInteractor.getCurrentTheme())
    }

    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
        const val THEME_KEY = "THEME_KEY"
    }
}