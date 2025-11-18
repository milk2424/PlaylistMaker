package com.example.playlistmaker.presentation.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.repository.SongFavouriteStateRepository
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val currentSong: Song,
    private val mediaPlayer: MediaPlayer,
    private val songFavouriteStateRepository: SongFavouriteStateRepository
) : ViewModel() {
    companion object {
        private const val GET_CURRENT_TIME_DELAY = 300L
    }

    init {
        checkIsSongFavourite(currentSong.trackId)
        preparePlayer()
    }

    private var observeCurrentTimeJob: Job? = null

    private val playerStateMutableLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    private val _isSongFavouriteState = MutableStateFlow(false)
    val isSongFavouriteState: StateFlow<Boolean> = _isSongFavouriteState

    private fun checkIsSongFavourite(id: String) {
        viewModelScope.launch {
            _isSongFavouriteState.emit(songFavouriteStateRepository.isSongFavourite(id))
        }
    }

    fun switchIsSongFavouriteState() {
        Log.i("dbcheck","${_isSongFavouriteState.value}")
        viewModelScope.launch {
            val currentState = isSongFavouriteState.value

            if (currentState) songFavouriteStateRepository.removeSongFromFavourite(currentSong)
            else songFavouriteStateRepository.addSongToFavourite(currentSong)

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

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}
