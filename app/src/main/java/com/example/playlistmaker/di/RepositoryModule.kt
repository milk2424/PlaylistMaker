package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.App.Companion.SHARED_PREFS
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SongsRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.repository.SongsRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SongsRepository> {
        SongsRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single {
        androidContext()
            .applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    }

    single {
        Gson()
    }
}