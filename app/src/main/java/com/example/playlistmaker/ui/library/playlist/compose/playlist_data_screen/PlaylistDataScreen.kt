package com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistDataViewModel
import com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.bottom_sheet.MoreBottomSheet
import com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.bottom_sheet.SongsBottomSheet
import com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.dialog.DeleteType
import com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.dialog.RemoveDialog
import com.example.playlistmaker.ui.theme.LightGray
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDataScreen(
    playlist: Playlist,
    viewModel: PlaylistDataViewModel = koinViewModel(parameters = { parametersOf(playlist) }),
    onSongClicked: (Song) -> Unit,
    onEditPlaylistClicked: (Playlist) -> Unit
) {
    val songs by viewModel.songs.collectAsStateWithLifecycle()

    val stringFormat = stringResource(R.string.playlist_sharing_song_format)

    val minuteString = pluralStringResource(R.plurals.minute_plurals, songs.size, songs.size)

    var showMoreSheet by remember { mutableStateOf(false) }

    val moreSheetState = rememberModalBottomSheetState()

    val playlistInfo by viewModel.playlistMainInfo.collectAsStateWithLifecycle()

    val shouldShowDialog = remember { mutableStateOf(false) }

    var dialogDelete by remember { mutableStateOf<DeleteType?>(null) }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        viewModel.loadPlaylistInfo(playlist.id!!)
    }

    SongsBottomSheet(
        songs = songs,
        onSongClicked = onSongClicked,
        onSongLongClicked = { song ->
            dialogDelete = DeleteType.DeleteSong(song)
            shouldShowDialog.value = true
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
            ) {
                PlaylistMainData(viewModel)
                PlaylistButtons(onShareClick = {
                    viewModel.sharePlaylist(
                        minuteString,
                        stringFormat,
                    )
                }, onMoreClick = {
                    showMoreSheet = true
                })
            }
        }
    )

    if (showMoreSheet) {
        MoreBottomSheet(
            sheetState = moreSheetState,
            onDismissRequest = { showMoreSheet = false },
            playlist = playlistInfo,
            onShareClicked = {
                viewModel.sharePlaylist(
                    minuteString,
                    stringFormat,
                )
            },
            onEditPlaylistClicked = {
                onEditPlaylistClicked(playlistInfo)
            },
            onDeletePlaylist = {
                dialogDelete = DeleteType.DeletePlaylist(playlistInfo)
                shouldShowDialog.value = true
            }
        )
    }

    dialogDelete?.let { type ->
        RemoveDialog(
            visible = shouldShowDialog.value,
            text = when (type) {
                is DeleteType.DeletePlaylist -> stringResource(
                    R.string.dialog_delete_playlist_title,
                    type.playlist.name
                )

                is DeleteType.DeleteSong -> stringResource(
                    R.string.dialog_delete_song_from_playlist_title,
                )
            },
            onDismissRequest = { shouldShowDialog.value = false },
            onConfirmation = {
                when (type) {
                    is DeleteType.DeletePlaylist -> {
                        viewModel.deletePlaylist(type.playlist.id!!)
                        onBackPressedDispatcher?.onBackPressed()
                    }

                    is DeleteType.DeleteSong -> {
                        viewModel.deleteSongFromPlaylist(playlistInfo.id!!, type.song.trackId)
                    }
                }
            }
        )
    }

}