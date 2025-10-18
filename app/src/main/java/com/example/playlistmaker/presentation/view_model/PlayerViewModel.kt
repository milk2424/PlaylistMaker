package com.example.playlistmaker.presentation.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val songUrl: String, private val mediaPlayer: MediaPlayer) :
    ViewModel() {
    companion object {
        private const val GET_CURRENT_TIME_DELAY = 300L
    }

    init {
        preparePlayer()
    }

    private var observeCurrentTimeJob: Job? = null

    private val playerStateMutableLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    private fun preparePlayer() {
        mediaPlayer.setDataSource(songUrl)
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

    fun onPause() {
        pausePlayer()
    }

    private fun observeCurrentTime() {
        observeCurrentTimeJob?.cancel()
        observeCurrentTimeJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(GET_CURRENT_TIME_DELAY)
                if (mediaPlayer.isPlaying)
                    playerStateMutableLiveData.postValue(PlayerState.Playing(getCurrentTimeMapped()))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}
