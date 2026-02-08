package com.example.playlistmaker.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.SearchFragmentCompose
import com.example.playlistmaker.ui.theme.ColorTheme

@Preview(showSystemUi = true)
@Composable
fun ActivityScreen() {

    ColorTheme() {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                SearchFragmentCompose()
            }
        }
    }
}