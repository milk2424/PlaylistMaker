package com.example.playlistmaker.ui.library.compose.pager

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun TabName(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontSize = 14.sp
    )
}