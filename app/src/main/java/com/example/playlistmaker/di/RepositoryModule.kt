package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.App.Companion.SHARED_PREFS
import com.example.playlistmaker.data.favourite_songs.impl.FavouriteSongsRepositoryImpl
import com.example.playlistmaker.data.favourite_songs.impl.PlaylistDataRepositoryImpl
import com.example.playlistmaker.data.favourite_songs.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.data.image_storage.impl.ImageStorageRepositoryImpl
import com.example.playlistmaker.data.image_storage.repository.ImageStorageRepository
import com.example.playlistmaker.data.player.impl.SongFavouriteStateRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SongsRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.favourite_songs.repository.FavouriteSongsRepository
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistDataRepository
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository
import com.example.playlistmaker.domain.player.repository.SongFavouriteStateRepository
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

    single<FavouriteSongsRepository> {
        FavouriteSongsRepositoryImpl(get(), get())
    }

    single<SongFavouriteStateRepository> {
        SongFavouriteStateRepositoryImpl(get(), get(), get())
    }

    single<ImageStorageRepository> {
        ImageStorageRepositoryImpl(get())
    }

    single<PlaylistsRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<PlaylistDataRepository> {
        PlaylistDataRepositoryImpl(get(), get(), get())
    }

    single {
        androidContext()
            .applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    }

    single {
        Gson()
    }
}