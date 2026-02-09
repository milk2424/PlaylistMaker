package com.example.playlistmaker.ui.search.song_history

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.search.song_preview.SongItem

@Composable
fun SongsList(modifier: Modifier, songs: List<Song>) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(songs) { song ->
            SongItem(song)
        }
    }
}