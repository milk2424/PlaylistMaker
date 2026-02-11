package com.example.playlistmaker.ui.core

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun MainButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(24.dp),
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.background
        )
    }
}