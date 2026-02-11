package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.dialog

import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song

sealed class DeleteType {
    class DeleteSong(val song: Song) : DeleteType()
    class DeletePlaylist(val playlist: Playlist) : DeleteType()
}