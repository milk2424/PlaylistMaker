package com.example.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R


@Composable
fun SearchSongsTextField(/*viewModel: SearchViewModel*/) {
    //  val text by viewModel.inputText.collectAsStateWithLifecycle()
    val text by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = text,
        label = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painterResource(R.drawable.search_edit_text_icon),
                    contentDescription = stringResource(R.string.search),
                    modifier = Modifier.padding(8.dp)
                )
                Text(stringResource(R.string.search))
            }

        },
        onValueChange = { newText: String ->
            // viewModel.updateInputText(newText)
        },
        singleLine = true
    )
}