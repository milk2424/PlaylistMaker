package com.example.playlistmaker.ui.library.compose.pager.screens.playlists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R

@Composable
fun PlaylistItem(imagePath: String?, name: String, count: Int) {
    Column(modifier = Modifier.width(160.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = if (imagePath.isNullOrEmpty()) R.drawable.no_track_art else imagePath,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(bottom = 4.dp),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.width(160.dp),
            textAlign = TextAlign.Start,
            text = name,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
        Text(
            modifier = Modifier.width(160.dp),
            textAlign = TextAlign.Start,
            text = pluralStringResource(R.plurals.tracks_plurals, count, count),
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}