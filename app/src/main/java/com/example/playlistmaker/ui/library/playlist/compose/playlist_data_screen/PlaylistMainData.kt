package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistDataViewModel
import com.example.playlistmaker.ui.theme.Black
import com.example.playlistmaker.ui.theme.Gray

@Composable
fun PlaylistMainData(viewModel: PlaylistDataViewModel) {

    val playlistTime by viewModel.playlistTime.collectAsStateWithLifecycle()

    val songs by viewModel.songs.collectAsStateWithLifecycle()

    val playlist by viewModel.playlistMainInfo.collectAsStateWithLifecycle()

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Column {

        Box {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(bottom = 16.dp),
                model = if (playlist.image.isNullOrEmpty()) R.drawable.no_track_art else playlist.image,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray)
            )

            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        onBackPressedDispatcher?.onBackPressed()
                    },
                painter = painterResource(R.drawable.btn_arrow_back),
                tint = Black,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = playlist.name,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_bold)),
            color = Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = playlist.description,
            fontSize = 18.sp,
            color = Black,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 4.dp, top = 4.dp, bottom = 4.dp),
                text = pluralStringResource(R.plurals.minute_plurals, playlistTime, playlistTime),
                fontSize = 18.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Icon(
                modifier = Modifier.padding(horizontal = 4.dp),
                painter = painterResource(R.drawable.track_ellipse),
                tint = Black,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                text = pluralStringResource(R.plurals.tracks_plurals, songs.size, songs.size),
                fontSize = 18.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}