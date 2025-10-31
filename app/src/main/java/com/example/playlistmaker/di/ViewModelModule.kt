package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.view_model.FavouriteSongsViewModel
import com.example.playlistmaker.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (song: Song) ->
        PlayerViewModel(song, get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavouriteSongsViewModel(get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }
}