package com.example.playlistmaker.ui.search.song_history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun SongsHistory(songs: List<Song>, onHistoryClearClick: () -> Unit) {
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
            SongsList(Modifier.weight(1f), songs)
            Button(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    onHistoryClearClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.clear_history),
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
}