package com.example.playlistmaker.presentation.view_model.library.playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.use_cases.AddNewPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val newPlaylistUseCase: AddNewPlaylistUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<Pair<Boolean, String>> =
        MutableStateFlow(Pair(false, ""))

    val uiState = _uiState.asStateFlow()


    fun addNewPlaylist(name: String, description: String, selectedImageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO){
            newPlaylistUseCase(name, description, selectedImageUri).collect { isNewPlaylistAdded ->
                _uiState.value = isNewPlaylistAdded
            }
        }
    }
}