package com.example.playlistmaker.ui.library.playlist.compose.create_update_playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.example.playlistmaker.ui.core.ScreenName
import com.example.playlistmaker.ui.theme.Gray
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun PlaylistCreateScreen(
    playlist: Playlist? = null,
    viewModel: NewPlaylistViewModel = koinViewModel(parameters = { parametersOf(playlist) })
) {
    Column {
        ScreenName(
            stringResource(if (playlist == null) R.string.new_playlist else R.string.edit_playlist),
            true
        )
        AsyncImage(
            model = if (playlist?.image.isNullOrEmpty()) R.drawable.btn_add_photo else playlist.image,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .drawWithContent {
                    drawRoundRect(
                        color = Gray,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(8f, 8f),
                                phase = 0f
                            )
                        ),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )
                },
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}