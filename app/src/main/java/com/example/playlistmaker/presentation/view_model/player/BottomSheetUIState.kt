package com.example.playlistmaker.presentation.view_model.player

import com.example.playlistmaker.domain.model.Playlist

sealed interface BottomSheetUIState {
    data object Default : BottomSheetUIState
    data class Data(val playlists: List<Playlist>) : BottomSheetUIState
}