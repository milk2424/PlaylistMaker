package com.example.playlistmaker.ui.player.compose.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.ui.theme.Blue
import com.example.playlistmaker.ui.theme.Gray
import com.example.playlistmaker.ui.theme.White

@Composable
fun ActionButton(@DrawableRes res: Int, onClickAction: () -> Unit) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable { onClickAction() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(res),
            contentDescription = null,
            contentScale = ContentScale.Fit,
//            colorFilter = ColorFilter.tint(White)
        )
    }
}