package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.ui.player.compose.bottom_sheet.BottomSheetPlaylistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    sheetState: SheetState,
    playlist: Playlist,
    onShareClicked: () -> Unit,
    onEditPlaylistClicked: () -> Unit,
    onDeletePlaylist: (Playlist) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest =  onDismissRequest) {
        BottomSheetPlaylistItem(playlist, Modifier.padding(horizontal = 12.dp))

        MoreBottomSheetButton(
            text = stringResource(R.string.share),
            modifier = Modifier.clickable { onShareClicked() })

        MoreBottomSheetButton(
            text = stringResource(R.string.edit_playlist),
            modifier = Modifier.clickable {
                onEditPlaylistClicked()
            })

        MoreBottomSheetButton(
            text = stringResource(R.string.delete_playlist),
            modifier = Modifier.clickable {
                onDeletePlaylist(playlist)
            })
    }
}