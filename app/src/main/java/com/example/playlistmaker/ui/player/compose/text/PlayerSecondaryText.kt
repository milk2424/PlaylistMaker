package com.example.playlistmaker.ui.player.compose.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun PlayerSecondaryText(text: String, color: Color) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
        text = text,
        color = color,
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 13.sp
    )
}