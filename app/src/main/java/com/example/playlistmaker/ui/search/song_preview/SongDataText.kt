package com.example.playlistmaker.ui.search.song_preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.example.playlistmaker.R

@Composable
fun SongDataText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier,textColor: Color) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        overflow = TextOverflow.Ellipsis,
        color = textColor,
        maxLines = 1,
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        )
    )
}