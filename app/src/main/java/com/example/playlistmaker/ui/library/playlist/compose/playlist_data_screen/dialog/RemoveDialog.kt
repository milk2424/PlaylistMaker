package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.theme.Black
import com.example.playlistmaker.ui.theme.Blue
import com.example.playlistmaker.ui.theme.White

@Composable
fun RemoveDialog(
    visible: Boolean,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            containerColor = White,
            text = { Text(text = text, color = Black) },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = stringResource(R.string.no).uppercase(), color = Blue)
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {
                        onConfirmation()
                        onDismissRequest()
                    }
                ) {
                    Text(text = stringResource(R.string.yes).uppercase(), color = Blue)
                }
            }
        )
    }
}