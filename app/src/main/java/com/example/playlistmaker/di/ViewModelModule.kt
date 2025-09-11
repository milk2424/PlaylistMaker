package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (songUrl: String) ->
        PlayerViewModel(songUrl, get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    factory <MediaPlayer> {
        MediaPlayer()
    }
}