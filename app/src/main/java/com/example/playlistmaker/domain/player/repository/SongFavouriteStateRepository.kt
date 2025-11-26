package com.example.playlistmaker.domain.player.repository

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.search.model.Song

interface SongFavouriteStateRepository {
    suspend fun addSongToFavourite(song: Song)

    suspend fun removeSongFromFavourite(song: Song)

    suspend fun isSongFavourite(id: String): Boolean

    suspend fun addSongToPlaylist(song: Song, playlistId: Int)

    fun getPlaylistSongsIds(playlistId: Int): List<String>

}