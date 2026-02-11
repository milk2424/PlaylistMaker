package com.example.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R


@Composable
fun SearchError(isNetworkError: Boolean = true, onNetworkErrorRetryClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(if (isNetworkError) R.drawable.bad_connection else R.drawable.no_tracks),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(if (isNetworkError) R.string.bad_connection_error_text else R.string.empty_track_list_text),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 19.sp,
        )
        if (isNetworkError)
            Button(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    onNetworkErrorRetryClick?.invoke()
                }
            ) {
                Text(
                    text = stringResource(R.string.search_update_button),
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    color = MaterialTheme.colorScheme.background
                )
            }
    }

}