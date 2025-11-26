package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.view_model.player.PlayerViewModel
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import com.example.playlistmaker.presentation.view_model.library.FavouriteSongsViewModel
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistDataViewModel
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (song: Song) ->
        PlayerViewModel(song, get(), get(), get())
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

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel{
        PlaylistDataViewModel(get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }
}