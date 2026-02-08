package com.example.playlistmaker.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.core.ScreenName
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchFragmentCompose(/*viewModel: SearchViewModel = koinViewModel()*/) {
    Column(modifier = Modifier.fillMaxSize()) {
        ScreenName("Поиск", true)
        SearchSongsTextField()
    }

}