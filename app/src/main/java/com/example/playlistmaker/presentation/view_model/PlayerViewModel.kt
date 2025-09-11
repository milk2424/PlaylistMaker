package com.example.playlistmaker.presentation.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState

class PlayerViewModel(private val songUrl: String, private val mediaPlayer: MediaPlayer) :
    ViewModel() {
    companion object {
        private const val GET_CURRENT_TIME_DELAY = 200L
    }

    init {
        preparePlayer()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val currentTimeRunnable = object : Runnable {
        override fun run() {
            if (playerStateMutableLiveData.value is PlayerState.Playing) {
                playerStateMutableLiveData.postValue(PlayerState.Playing(getCurrentTimeMapped()))
                handler.postDelayed(this, GET_CURRENT_TIME_DELAY)
            }
        }
    }

    private val playerStateMutableLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    fun preparePlayer() {
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateMutableLiveData.postValue(PlayerState.Prepared())
            startPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            playerStateMutableLiveData.postValue(PlayerState.Prepared())
            pauseTimer()
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
        playerStateMutableLiveData.postValue(PlayerState.Playing(getCurrentTimeMapped()))
        handler.postDelayed(currentTimeRunnable, GET_CURRENT_TIME_DELAY)
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateMutableLiveData.postValue(PlayerState.Paused(getCurrentTimeMapped()))
    }

    private fun pauseTimer() {
        handler.removeCallbacks(currentTimeRunnable)
    }

    private fun getCurrentTimeMapped() = PlayerTimeMapper.map(mediaPlayer.currentPosition)

    fun onPause() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        pauseTimer()
    }
}
