package com.example.playlistmaker.ui.library.compose.pager.screens.favourite_songs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.favourite_songs.FavouriteSongsState
import com.example.playlistmaker.presentation.view_model.library.FavouriteSongsViewModel
import com.example.playlistmaker.ui.core.SongsList
import com.example.playlistmaker.ui.library.compose.pager.Error
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteSongsScreen(
    viewModel: FavouriteSongsViewModel = koinViewModel(),
    onSongClicked: (Song) -> Unit
) {
    val screenState by viewModel.observeFavouriteSongsState().observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavouriteSongs()
    }

    when (screenState) {
        is FavouriteSongsState.Data -> {
            SongsList(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                (screenState as FavouriteSongsState.Data).songs,
                onSongClicked
            )
        }

        else -> {
            Error(stringResource(R.string.your_library_is_empty))
        }
    }
}