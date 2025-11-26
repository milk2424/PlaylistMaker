package com.example.playlistmaker.presentation.view_model.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.repository.FavouriteSongsRepository
import com.example.playlistmaker.presentation.utils.favourite_songs.FavouriteSongsState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteSongsViewModel(private val repository: FavouriteSongsRepository) : ViewModel() {

    private val _favouriteSongsState =
        MutableLiveData<FavouriteSongsState>(FavouriteSongsState.Empty)

    fun observeFavouriteSongsState(): LiveData<FavouriteSongsState> = _favouriteSongsState


    fun loadFavouriteSongs() {
        viewModelScope.launch {
            repository
                .getFavoriteSongs()
                .catch { }
                .collect { songs ->
                    when {
                        songs.isEmpty() -> _favouriteSongsState.postValue(FavouriteSongsState.Empty)
                        else -> _favouriteSongsState.postValue(FavouriteSongsState.Data(songs.reversed()))
                    }
                }
        }
    }
}