package com.example.playlistmaker.ui.player.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.player.BottomSheetUIState
import com.example.playlistmaker.presentation.view_model.player.PlayerViewModel
import com.example.playlistmaker.ui.core.ScreenName
import com.example.playlistmaker.ui.player.compose.bottom_sheet.AddSongToPlaylistBottomSheet
import com.example.playlistmaker.ui.player.compose.button.ActionButton
import com.example.playlistmaker.ui.player.compose.button.PlaybackButton
import com.example.playlistmaker.ui.player.compose.text.PlayerMainText
import com.example.playlistmaker.ui.player.compose.text.PlayerSecondaryData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    song: Song,
    buttonBackClicked: () -> Unit,
    newPlaylistClicked: () -> Unit
) {

    var isBackNavigation by remember { mutableStateOf(false) }

    BackHandler {
        isBackNavigation = true
        buttonBackClicked()
    }

    val scrollState = rememberScrollState()

    val playerState by viewModel.playerStateLiveData().observeAsState()

    val isSongFavourite by viewModel.isSongFavouriteState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    val playlists by viewModel.bottomSheetDataState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState()

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    if (!isBackNavigation)
                        viewModel.startMusicPlayerService()
                }

                Lifecycle.Event.ON_START -> {
                    viewModel.removeNotification()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            viewModel.removeNotification()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenName("", true, Modifier.clickable {
            isBackNavigation = true
            buttonBackClicked()
        })
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = song.artworkUrl100,
            contentDescription = null,
            placeholder = painterResource(R.drawable.no_track_art),
            contentScale = ContentScale.Crop
        )
        PlayerMainText(
            text = song.trackName,
            fontSize = 22.sp
        )
        PlayerMainText(
            text = song.artistName,
            fontSize = 14.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(R.drawable.btn_add_to_library) {
                isBottomSheetVisible = true
                viewModel.loadPlaylists()
                scope.launch { bottomSheetState.partialExpand() }
            }
            PlaybackButton(playerState!!) {
                viewModel.buttonPlayClicked()
            }
            ActionButton(if (!isSongFavourite) R.drawable.btn_save_to_favorite_inactive else R.drawable.btn_save_to_favourite_active) {
                viewModel.switchIsSongFavouriteState()
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = playerState?.time ?: "00:00",
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )

        PlayerSecondaryData(song)
    }
    when (playlists) {
        is BottomSheetUIState.Data ->
            AddSongToPlaylistBottomSheet(
                isBottomSheetVisible,
                (playlists as BottomSheetUIState.Data).playlists,
                bottomSheetState,
                song,
                { song, playlist ->
                    viewModel.addSongToPlaylist(song, playlist)
                },
                onDismiss = {
                    isBottomSheetVisible = false
                },
                hideBottomSheet = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            isBottomSheetVisible = false
                        }
                    }

                }
            ) {
                newPlaylistClicked()
            }

        BottomSheetUIState.Default -> {}
    }

}