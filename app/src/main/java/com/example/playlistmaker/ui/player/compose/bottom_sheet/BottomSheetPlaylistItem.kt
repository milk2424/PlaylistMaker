package com.example.playlistmaker.ui.player.compose.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist

@Composable
fun BottomSheetPlaylistItem(playlist: Playlist, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (playlist.image.isNullOrEmpty()) R.drawable.no_track_art else playlist.image,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = pluralStringResource(
                    R.plurals.tracks_plurals,
                    playlist.songsCount,
                    playlist.songsCount
                ),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }
    }
}