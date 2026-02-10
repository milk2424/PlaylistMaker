package com.example.playlistmaker.ui.player.compose.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song

@Composable
fun BottomSheetPlaylists(
    playlists: List<Playlist>,
    song: Song,
    addSongToPlaylist: (song: Song, playlist: Playlist) -> Unit,
    hideBottomSheet: () -> Unit
) {
    LazyColumn {
        items(playlists) { playlist ->
            BottomSheetPlaylistItem(playlist, Modifier.clickable {
                addSongToPlaylist(song, playlist)
                hideBottomSheet()
            })
        }
    }
}