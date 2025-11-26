package com.example.playlistmaker.domain.favourite_songs.repository

import com.example.playlistmaker.domain.search.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistDataRepository {

    fun loadSongs(playlistId:Int): Flow<List<Song>>

    fun getPlaylistTime(playlistId:Int): Long

}