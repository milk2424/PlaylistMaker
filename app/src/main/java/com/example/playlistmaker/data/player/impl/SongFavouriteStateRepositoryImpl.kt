package com.example.playlistmaker.data.player.impl

import com.example.playlistmaker.data.db.PlaylistMakerDB
import com.example.playlistmaker.data.favourite_songs.utils.SongEntityMapper
import com.example.playlistmaker.data.player.mapper.PlaylistSongEntityMapper
import com.example.playlistmaker.domain.player.repository.SongFavouriteStateRepository
import com.example.playlistmaker.domain.search.model.Song

class SongFavouriteStateRepositoryImpl(
    private val db: PlaylistMakerDB,
    private val songEntityMapper: SongEntityMapper,
    private val playlistSongEntityMapper: PlaylistSongEntityMapper
) : SongFavouriteStateRepository {
    override suspend fun addSongToFavourite(song: Song) {
        db.songsHistoryDao().addSongToFavourite(songEntityMapper.map(song))
    }

    override suspend fun removeSongFromFavourite(song: Song) {
        db.songsHistoryDao().removeSongFromFavourite(songEntityMapper.map(song))
    }

    override suspend fun isSongFavourite(id: String): Boolean {
        return !db.songsHistoryDao().isSongFavourite(id).isNullOrEmpty()
    }

    override suspend fun addSongToPlaylist(song: Song, playlistId: Int) {
        db.playlistDao().addSongToPlaylist(playlistSongEntityMapper.map(song, playlistId))
    }

    override fun getPlaylistSongsIds(playlistId: Int): List<String> {
        return db.playlistDao().getPlaylistSongsIds(playlistId)
    }
}