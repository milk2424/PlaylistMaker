package com.example.playlistmaker.ui.player.compose.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.ui.theme.Gray

@Composable
fun PlayerSecondaryData(song: Song) {
    val data = remember {
        listOfNotNull(
            R.string.track_duration to PlayerTimeMapper.map(song.trackTimeMillis),
            song.collectionName?.let { R.string.album to song.collectionName },
            song.releaseDate?.let { R.string.year to song.releaseDate.substringBefore("-") },
            R.string.genre to song.primaryGenreName,
            R.string.country to song.country
        )
    }
    Row(Modifier.fillMaxSize()) {
        Column {
            data.forEach { data -> PlayerSecondaryText(stringResource(data.first), Gray) }
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            data.forEach { data ->
                PlayerSecondaryText(
                    data.second,
                    MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}