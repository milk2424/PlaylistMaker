package com.example.playlistmaker.ui.settings.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun SettingsItem(
    text: String,
    onClickAction: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .clickable {
                onClickAction?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        trailingContent()
    }
}