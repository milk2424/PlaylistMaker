package com.example.playlistmaker.presentation.view_model.library.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.model.PlaylistState
import com.example.playlistmaker.domain.favourite_songs.use_cases.LoadPlaylistsUseCase
import com.example.playlistmaker.presentation.utils.playlist.PlaylistUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(private val loadPlaylistsUseCase: LoadPlaylistsUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<PlaylistUIState> =
        MutableStateFlow(PlaylistUIState.Loading)

    val uiState = _uiState.asStateFlow()

    fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            loadPlaylistsUseCase().collect { state ->
                when (state) {
                    is PlaylistState.Empty -> _uiState.value = PlaylistUIState.Empty
                    is PlaylistState.Loading -> _uiState.value = PlaylistUIState.Loading
                    is PlaylistState.Success -> _uiState.value = PlaylistUIState.Success(state.data)
                }
            }
        }
    }

}