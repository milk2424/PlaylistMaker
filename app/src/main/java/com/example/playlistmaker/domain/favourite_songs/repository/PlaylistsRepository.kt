package com.example.playlistmaker.domain.favourite_songs.repository

import android.net.Uri
import com.example.playlistmaker.domain.favourite_songs.model.Playlist

interface PlaylistsRepository {

    fun loadPlaylists(): List<Playlist>

    fun addNewPlaylist(name: String, description: String, imageUri: Uri?)

    fun addSongToPlaylist(playlistId: Int, songId: String)

    fun updatePlaylistInfo(playlistId: Int, name: String, description: String, uri: Uri?)
}