package com.example.playlistmaker.ui.search.song_preview

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.R

@Composable
fun SongImage(url: String) {
    AsyncImage(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(45.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop,
        model = url,
        placeholder = painterResource(R.drawable.no_track_art),
        contentDescription = null
    )
}