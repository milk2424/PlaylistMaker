package com.example.playlistmaker.ui.player.compose.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun PlayerMainText(text: String, fontSize: TextUnit) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = fontSize
    )
}