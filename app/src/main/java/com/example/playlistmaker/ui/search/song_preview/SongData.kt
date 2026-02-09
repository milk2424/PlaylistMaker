package com.example.playlistmaker.ui.search.song_preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun SongData(
    songName: String,
    artist: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Top
    )
    {
        SongDataText(songName, 16.sp, textColor = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SongDataText(
                artist,
                11.sp,
                Modifier.weight(1f, fill = false),
                MaterialTheme.colorScheme.inverseSurface
            )
            Image(
                modifier = Modifier.padding(horizontal = 5.dp),
                painter = painterResource(R.drawable.track_ellipse),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface),
                contentDescription = null,
            )
            SongDataText(time, 11.sp, textColor = MaterialTheme.colorScheme.inverseSurface)
        }
    }
}