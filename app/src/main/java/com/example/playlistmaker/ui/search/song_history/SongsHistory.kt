package com.example.playlistmaker.ui.search.song_history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.core.MainButton
import com.example.playlistmaker.ui.core.SongsList

@Composable
fun SongsHistory(
    songs: List<Song>,
    onSongClicked: (Song) -> Unit,
    onHistoryClearClick: () -> Unit
) {
    if (songs.isNotEmpty())
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                text = stringResource(R.string.your_search),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                textAlign = TextAlign.Center
            )
            SongsList(Modifier.weight(1f), songs, onSongClicked)
            MainButton(text = stringResource(R.string.clear_history)) {
                onHistoryClearClick()
            }
        }
}