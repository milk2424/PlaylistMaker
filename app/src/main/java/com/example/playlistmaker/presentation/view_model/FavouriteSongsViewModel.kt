package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.repository.FavouriteSongsRepository
import com.example.playlistmaker.presentation.utils.favourite_songs.FavouriteSongsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteSongsViewModel(private val repository: FavouriteSongsRepository) : ViewModel() {

    private val _favouriteSongsState =
        MutableStateFlow<FavouriteSongsState>(FavouriteSongsState.Loading)

    val favouriteSongsState: StateFlow<FavouriteSongsState> = _favouriteSongsState


    fun loadFavouriteSongs() {
        viewModelScope.launch {
            _favouriteSongsState.emit(FavouriteSongsState.Loading)
            repository
                .getFavoriteSongs()
                .catch { }
                .collect { songs ->
                    when {
                        songs.isEmpty() -> _favouriteSongsState.emit(FavouriteSongsState.Empty)
                        else -> _favouriteSongsState.emit(FavouriteSongsState.Data(songs.reversed()))
                    }
                }
        }
    }
}