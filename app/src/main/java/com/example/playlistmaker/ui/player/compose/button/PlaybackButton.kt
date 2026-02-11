package com.example.playlistmaker.ui.player.compose.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.utils.player.PlayerState


@Composable
fun PlaybackButton(playerState: PlayerState, onClick: () -> Unit) {
    val imageRes = when (playerState) {
        is PlayerState.Playing -> R.drawable.btn_pause_player
        else -> R.drawable.btn_start_player
    }
    Image(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        painter = painterResource(imageRes),
        contentScale = ContentScale.Fit,
        contentDescription = null,
        colorFilter = tint(MaterialTheme.colorScheme.primary)
    )
}