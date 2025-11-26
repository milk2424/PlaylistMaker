package com.example.playlistmaker.domain.favourite_songs.model

import com.example.playlistmaker.domain.model.Playlist

sealed interface PlaylistState {
    data object Loading: PlaylistState
    data object Empty: PlaylistState
    data class Success(val data:List<Playlist>): PlaylistState
}