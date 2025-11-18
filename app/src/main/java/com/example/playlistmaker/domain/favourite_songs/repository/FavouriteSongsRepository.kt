package com.example.playlistmaker.domain.favourite_songs.repository

import com.example.playlistmaker.domain.search.model.Song
import kotlinx.coroutines.flow.Flow

interface FavouriteSongsRepository {
    suspend fun getFavoriteSongs(): Flow<List<Song>>
}