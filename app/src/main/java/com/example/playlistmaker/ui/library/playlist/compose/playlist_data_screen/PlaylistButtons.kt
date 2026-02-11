package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.theme.Black


@Composable
fun PlaylistButtons(onShareClick: () -> Unit, onMoreClick: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Icon(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
                .clickable { onShareClick() },
            painter = painterResource(R.drawable.btn_share),
            contentDescription = null,
            tint = Black
        )
        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp)
                .clickable { onMoreClick() },
            painter = painterResource(R.drawable.btn_more),
            contentDescription = null,
            tint = Black
        )
    }
}