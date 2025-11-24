package com.example.playlistmaker.di

import com.example.playlistmaker.domain.favourite_songs.use_cases.AddNewPlaylistUseCase
import com.example.playlistmaker.domain.favourite_songs.use_cases.LoadPlaylistsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        AddNewPlaylistUseCase(get())
    }

    single {
        LoadPlaylistsUseCase(get())
    }
}