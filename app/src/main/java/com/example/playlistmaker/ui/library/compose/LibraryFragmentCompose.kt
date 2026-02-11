package com.example.playlistmaker.ui.library.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.core.ScreenName
import com.example.playlistmaker.ui.library.compose.pager.TabName
import com.example.playlistmaker.ui.library.compose.pager.screens.favourite_songs.FavouriteSongsScreen
import com.example.playlistmaker.ui.library.compose.pager.screens.playlists.PlaylistsScreen

@Composable
fun LibraryFragmentCompose(onSongClicked: (Song) -> Unit, onPlaylistClicked:(Playlist)->Unit,newPlaylistClicked:()->Unit) {

    val pagerState = rememberPagerState(pageCount = { 2 })

    var selectedTabIndex by remember { mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenName(stringResource(R.string.library), false)

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Transparent
                ),
            containerColor = Color.Transparent
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.primary,
                text = { TabName(stringResource(R.string.favourite_songs)) },
                onClick = { selectedTabIndex = 0 }
            )
            Tab(
                selected = selectedTabIndex == 1,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.primary,
                text = { TabName(stringResource(R.string.playlists)) },
                onClick = { selectedTabIndex = 1 }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavouriteSongsScreen(onSongClicked = onSongClicked)
                1 -> PlaylistsScreen(onPlaylistClicked = onPlaylistClicked, newPlaylistClicked = newPlaylistClicked)
            }
        }

    }
}