package com.example.playlistmaker.ui.library.compose.pager.screens.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.domain.favourite_songs.model.Playlist

@Composable
fun PlaylistsGrid(items: List<Playlist>, onPlaylistClicked: (Playlist) -> Unit) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        items(items) { playlist ->
            PlaylistItem(
                playlist.image,
                playlist.name,
                playlist.songsCount,
                Modifier
                    .width(160.dp)
                    .clickable { onPlaylistClicked(playlist) })
        }
    }
}
