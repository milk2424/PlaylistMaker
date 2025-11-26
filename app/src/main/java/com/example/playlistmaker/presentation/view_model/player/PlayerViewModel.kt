package com.example.playlistmaker.presentation.view_model.player

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.favourite_songs.model.PlaylistState
import com.example.playlistmaker.domain.favourite_songs.use_cases.LoadPlaylistsUseCase
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.BottomSheetUIState
import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val currentSong: Song,
    private val mediaPlayer: MediaPlayer,
    private val interactor: PlayerInteractor,
    private val loadPlaylistsUseCase: LoadPlaylistsUseCase
) : ViewModel() {
    companion object {
        private const val GET_CURRENT_TIME_DELAY = 300L
    }

    init {
        checkIsSongFavourite(currentSong.trackId)
        preparePlayer()
    }

    private val _bottomSheetDataState: MutableStateFlow<BottomSheetUIState> = MutableStateFlow(
        BottomSheetUIState.Default
    )

    val bottomSheetDataState = _bottomSheetDataState.asStateFlow()

    private var observeCurrentTimeJob: Job? = null

    private val playerStateMutableLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    private val _isSongFavouriteState = MutableStateFlow(false)
    val isSongFavouriteState: StateFlow<Boolean> = _isSongFavouriteState

    private val _isSongAddedToPlaylist = MutableSharedFlow<Pair<String, Boolean>>()
    val isSongAddedToPlaylist = _isSongAddedToPlaylist.asSharedFlow()


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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(currentSong.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateMutableLiveData.postValue(PlayerState.Prepared())
            startPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            playerStateMutableLiveData.postValue(PlayerState.Prepared())
        }
    }

    fun buttonPlayClicked() {
        when (playerStateMutableLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateMutableLiveData.value = PlayerState.Playing(getCurrentTimeMapped())
        observeCurrentTime()
    }

    private fun pausePlayer() {
        playerStateMutableLiveData.postValue(PlayerState.Paused(getCurrentTimeMapped()))
        mediaPlayer.pause()
    }

    private fun getCurrentTimeMapped() = PlayerTimeMapper.map(mediaPlayer.currentPosition)

    private fun observeCurrentTime() {
        observeCurrentTimeJob?.cancel()
        observeCurrentTimeJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(GET_CURRENT_TIME_DELAY)
                if (mediaPlayer.isPlaying) playerStateMutableLiveData.postValue(
                    PlayerState.Playing(
                        getCurrentTimeMapped()
                    )
                )
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}