package com.example.playlistmaker.presentation.view_model.library.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistDataRepository
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlaylistDataTimeMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDataViewModel(private val repository: PlaylistDataRepository) : ViewModel() {

    private val _playlistTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val playlistTime = _playlistTime.asStateFlow()

    private val _songs: MutableStateFlow<List<Song>> = MutableStateFlow(listOf())
    val songs = _songs.asStateFlow()

    private var _playlistMainInfo: MutableSharedFlow<Playlist> = MutableSharedFlow()
    val playlistMainInfo = _playlistMainInfo.asSharedFlow()

    private fun loadSongs(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadSongs(playlistId).collect { songs ->
                _songs.value = songs
            }
        }
    }

    fun loadPlaylistInfo(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadPlaylistById(playlistId).collect { playlist ->
                _playlistMainInfo.emit(playlist)
                getPlaylistTime(playlistId)
                loadSongs(playlistId)
            }
        }
    }

    private fun getPlaylistTime(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val time = repository.getPlaylistTime(playlistId)
            val mappedTimeMinutes = PlaylistDataTimeMapper.map(time)
            _playlistTime.value = mappedTimeMinutes
        }
    }

    fun deleteSongFromPlaylist(playlistId: Int, songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSongFromPlaylist(playlistId, songId)
            loadPlaylistInfo(playlistId)
        }
    }
}