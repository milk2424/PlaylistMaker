package com.example.playlistmaker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.utils.search.SongState
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.core.ScreenName
import com.example.playlistmaker.ui.search.song_history.SongsHistory
import com.example.playlistmaker.ui.search.song_history.SongsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchFragmentCompose(viewModel: SearchViewModel = koinViewModel()) {
    val text by viewModel.inputText.collectAsStateWithLifecycle()
    val screenState by viewModel.songStateLiveData().observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    fun hideKeyboard() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ScreenName(stringResource(R.string.search), false)
        SearchSongsTextField(
            text,
            onValueChange = { newText -> viewModel.updateInputText(newText) },
            onClearClick = {
                hideKeyboard()
                viewModel.clearInputText()
            },
            onDoneClick = {
                hideKeyboard()
                viewModel.searchSongsByDoneAction()
            })
        when (screenState) {
            is SongState.Empty -> SearchError(false)
            is SongState.History -> SongsHistory((screenState as SongState.History).songs) {
                viewModel.clearHistory()
            }

            is SongState.Loading -> Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp), color = Color(0xFF3772E7)
                )
            }

            is SongState.NetworkError, null -> SearchError(
                true
            ) { viewModel.searchSongsByDoneAction() }

            is SongState.Successful -> {
                SongsList(Modifier.weight(1f), (screenState as SongState.Successful).songs)
            }
        }
    }
}