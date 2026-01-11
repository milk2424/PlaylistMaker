package com.example.playlistmaker.presentation.view_model.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.favourite_songs.model.PlaylistState
import com.example.playlistmaker.domain.favourite_songs.use_cases.LoadPlaylistsUseCase
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.repository.MusicPlayer
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.player.BottomSheetUIState
import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val currentSong: Song,
    private val interactor: PlayerInteractor,
    private val loadPlaylistsUseCase: LoadPlaylistsUseCase
) : ViewModel() {

    init {
        checkIsSongFavourite(currentSong.trackId)
    }

    private val _bottomSheetDataState: MutableStateFlow<BottomSheetUIState> = MutableStateFlow(
        BottomSheetUIState.Default
    )

    val bottomSheetDataState = _bottomSheetDataState.asStateFlow()

    private val playerStateMutableLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    private val _isSongFavouriteState = MutableStateFlow(false)
    val isSongFavouriteState: StateFlow<Boolean> = _isSongFavouriteState

    private val _isSongAddedToPlaylist = MutableSharedFlow<Pair<String, Boolean>>()
    val isSongAddedToPlaylist = _isSongAddedToPlaylist.asSharedFlow()

    private var musicPlayer: MusicPlayer? = null

    private fun checkIsSongFavourite(id: String) {
        viewModelScope.launch {
            _isSongFavouriteState.emit(interactor.isSongFavourite(id))
        }
    }

    fun switchIsSongFavouriteState() {
        viewModelScope.launch {
            val currentState = isSongFavouriteState.value

            if (currentState) interactor.removeSongFromFavourite(currentSong)
            else interactor.addSongToFavourite(currentSong)

            _isSongFavouriteState.emit(!currentState)
        }
    }

    fun buttonPlayClicked() {
        when (playerStateMutableLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun startPlayer() {
        musicPlayer?.start()
    }

    private fun pausePlayer() {
        musicPlayer?.pause()
    }

    fun onPause() {
        pausePlayer()
    }

    fun loadPlaylists() {
        if (_bottomSheetDataState.value is BottomSheetUIState.Default)
            viewModelScope.launch(Dispatchers.IO) {
                loadPlaylistsUseCase().collect { state ->
                    when (state) {
                        is PlaylistState.Empty -> BottomSheetUIState.Data(emptyList())
                        is PlaylistState.Loading -> {}
                        is PlaylistState.Success ->
                            _bottomSheetDataState.value = BottomSheetUIState.Data(state.data)
                    }
                }
            }
    }

    fun resetPlaylistsData() {
        if (_bottomSheetDataState.value is BottomSheetUIState.Data) _bottomSheetDataState.value =
            BottomSheetUIState.Default
    }

    fun addSongToPlaylist(song: Song, playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val isSongInPlaylist = interactor.getPlaylistSongsIds(playlist.id!!)
                .contains(song.trackId)
            if (!isSongInPlaylist) {
                interactor.addSongToPlaylist(song, playlist.id)
                _isSongAddedToPlaylist.emit(Pair(playlist.name, true))
            } else {
                _isSongAddedToPlaylist.emit(Pair(playlist.name, false))
            }
        }
    }

    fun setupMusicPlayer(player: MusicPlayer) {
        musicPlayer = player
        viewModelScope.launch {
            musicPlayer?.getPlayerState()?.collect { state ->
                playerStateMutableLiveData.postValue(state)
            }
        }
    }

    fun removeMusicPlayer() {
        musicPlayer = null
    }

    override fun onCleared() {
        musicPlayer = null
        super.onCleared()
    }

    fun needToStartForegroundService() = playerStateMutableLiveData.value is PlayerState.Playing
}