package com.example.playlistmaker.presentation.utils.player

import com.example.playlistmaker.domain.favourite_songs.model.Playlist

sealed interface BottomSheetUIState {
    data object Default : BottomSheetUIState
    data class Data(val playlists: List<Playlist>) : BottomSheetUIState
}