package com.example.playlistmaker.ui.library.playlist

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistUIState {
    data object Loading : PlaylistUIState
    data object Empty : PlaylistUIState
    data class Success(val data: List<Playlist>) : PlaylistUIState
}
