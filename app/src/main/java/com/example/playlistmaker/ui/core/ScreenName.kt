package com.example.playlistmaker.ui.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun ScreenName(text: String, isButtonBackVisible: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isButtonBackVisible) Image(
            painter = painterResource(R.drawable.btn_arrow_back),
            contentDescription = stringResource(R.string.btn_back),
            modifier = Modifier.padding(start = 16.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}