package com.example.playlistmaker.data.favourite_songs.impl

import com.example.playlistmaker.data.db.PlaylistMakerDB
import com.example.playlistmaker.data.db.entity.SongEntity
import com.example.playlistmaker.data.favourite_songs.utils.SongEntityMapper
import com.example.playlistmaker.domain.favourite_songs.repository.FavouriteSongsRepository
import kotlinx.coroutines.flow.flow

class FavouriteSongsRepositoryImpl(
    private val db: PlaylistMakerDB,
    private val mapper: SongEntityMapper
) : FavouriteSongsRepository {

    override suspend fun getFavoriteSongs() = flow {
        val songs = db.songsHistoryDao().getFavoriteSongs()
        emit(mapSongs(songs))
    }

    private fun mapSongs(songs: List<SongEntity>) = songs.map { mapper.map(it) }
}