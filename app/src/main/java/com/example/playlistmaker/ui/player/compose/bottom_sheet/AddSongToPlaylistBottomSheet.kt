package com.example.playlistmaker.ui.player.compose.bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.core.MainButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongToPlaylistBottomSheet(
    visible: Boolean,
    playlists: List<Playlist>,
    bottomSheetState: SheetState,
    song: Song,
    addSongToPlaylist: (song: Song, playlist: Playlist) -> Unit,
    onDismiss: () -> Unit,
    hideBottomSheet: () -> Unit,
    createNewPlaylist: () -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = bottomSheetState
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.add_to_playlist),
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                MainButton(stringResource(R.string.new_playlist)) {
                    createNewPlaylist()
                }
                BottomSheetPlaylists(playlists,song,addSongToPlaylist,hideBottomSheet)
            }
        }
    }
}