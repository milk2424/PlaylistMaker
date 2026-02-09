package com.example.playlistmaker.ui.library.compose.pager.screens.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.utils.playlist.PlaylistUIState
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.core.MainButton
import com.example.playlistmaker.ui.core.ProgressIndicator
import com.example.playlistmaker.ui.library.compose.pager.Error
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(viewModel: PlaylistViewModel = koinViewModel()) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MainButton(stringResource(R.string.new_playlist)) {

            }
        }

        when (screenState) {
            is PlaylistUIState.Empty -> Error(stringResource(R.string.you_dont_create_any_playlists))
            is PlaylistUIState.Loading -> ProgressIndicator()
            is PlaylistUIState.Success -> {
                PlaylistsGrid((screenState as PlaylistUIState.Success).data)
            }

        }
    }

}