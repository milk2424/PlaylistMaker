package com.example.playlistmaker.data.player.impl

import com.example.playlistmaker.data.db.PlaylistMakerDB
import com.example.playlistmaker.data.favourite_songs.utils.SongEntityMapper
import com.example.playlistmaker.domain.player.repository.SongFavouriteStateRepository
import com.example.playlistmaker.domain.search.model.Song

class SongFavouriteStateRepositoryImpl(
    private val db: PlaylistMakerDB,
    private val mapper: SongEntityMapper
) : SongFavouriteStateRepository {
    override suspend fun addSongToFavourite(song: Song) {
        db.songsHistoryDao().addSongToFavourite(mapper.map(song))
    }

    override suspend fun removeSongFromFavourite(song: Song) {
        db.songsHistoryDao().removeSongFromFavourite(mapper.map(song))
    }

    override suspend fun isSongFavourite(id: String): Boolean {
        return !db.songsHistoryDao().isSongFavourite(id).isNullOrEmpty()
    }
}