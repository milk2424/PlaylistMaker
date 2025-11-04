package com.example.playlistmaker.presentation.utils.favourite_songs

import com.example.playlistmaker.domain.search.model.Song

sealed interface FavouriteSongsState {
    data object Empty : FavouriteSongsState
    data class Data(val songs: List<Song>) : FavouriteSongsState
}