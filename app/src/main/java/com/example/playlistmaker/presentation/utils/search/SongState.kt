package com.example.playlistmaker.presentation.utils.search

import com.example.playlistmaker.domain.search.model.Song

sealed interface SongState {
    data object Loading : SongState
    data object NetworkError : SongState
    data object Empty : SongState
    data class Successful(val songs: List<Song>):SongState
    data class History(val songs: List<Song>):SongState
}