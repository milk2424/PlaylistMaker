package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.bottom_sheet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.core.SongsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsBottomSheet(
    songs: List<Song>,
    onSongClicked: (Song) -> Unit,
    onSongLongClicked: (Song) -> Unit,
    content: @Composable () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 180.dp,
        sheetSwipeEnabled = true,
        containerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            SongsList(
                modifier = Modifier.fillMaxSize(),
                songs = songs,
                onItemClick = onSongClicked,
                onItemLongClicked = onSongLongClicked
            )
        }) {
        content()
    }
}