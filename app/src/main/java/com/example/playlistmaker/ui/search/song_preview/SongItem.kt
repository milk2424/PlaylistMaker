package com.example.playlistmaker.ui.search.song_preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper

@Composable
fun SongItem(song: Song, onItemClick: (Song) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(Color.Transparent)
            .clickable {
                onItemClick.invoke(song)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SongImage(song.artworkUrl100)
        SongData(
            song.trackName,
            song.artistName,
            PlayerTimeMapper.map(song.trackTimeMillis),
            modifier = Modifier.weight(1f)
        )
        Image(
            painterResource(R.drawable.btn_arrow_forward),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
        )
    }
}