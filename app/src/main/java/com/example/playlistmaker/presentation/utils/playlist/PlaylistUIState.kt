package com.example.playlistmaker.presentation.utils.playlist

import com.example.playlistmaker.domain.favourite_songs.model.Playlist

sealed interface PlaylistUIState {
    data object Loading : PlaylistUIState
    data object Empty : PlaylistUIState
    data class Success(val data: List<Playlist>) : PlaylistUIState
}