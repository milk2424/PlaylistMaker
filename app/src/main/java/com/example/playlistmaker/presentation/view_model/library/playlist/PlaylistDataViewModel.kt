package com.example.playlistmaker.presentation.view_model.library.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistDataRepository
import com.example.playlistmaker.presentation.mapper.player_mapper.PlaylistDataTimeMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDataViewModel(private val repository: PlaylistDataRepository) : ViewModel() {

    private val _playlistTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val playlistTime = _playlistTime.asStateFlow()

    fun loadSongs() {
        viewModelScope.launch {

        }
    }

    fun getPlaylistTime(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val time = repository.getPlaylistTime(playlistId)
            val mappedTimeMinutes = PlaylistDataTimeMapper.map(time)
            _playlistTime.value = mappedTimeMinutes
        }
    }
}