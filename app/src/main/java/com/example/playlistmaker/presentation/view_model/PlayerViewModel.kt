package com.example.playlistmaker.presentation.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import java.io.IOException

class PlayerViewModel(private val songUrl: String) : ViewModel() {
    companion object {
        fun getFactory(songUrl: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (songUrl != null) PlayerViewModel(songUrl) else throw IOException("songUrl is null")
            }
        }

        private const val GET_CURRENT_TIME_DELAY = 200L
    }

    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val currentTimeRunnable = object : Runnable {
        override fun run() {
            if (playerStateMutableLiveData.value == PlayerState.PLAYING) {
                currentTimeMutableLiveData.postValue(PlayerTimeMapper.map(mediaPlayer.currentPosition))
                handler.postDelayed(this, GET_CURRENT_TIME_DELAY)
            }
        }
    }

    private val playerStateMutableLiveData = MutableLiveData(PlayerState.DEFAULT)
    fun playerStateLiveData(): LiveData<PlayerState> = playerStateMutableLiveData

    private val currentTimeMutableLiveData = MutableLiveData("00:00")
    fun currentTimeLiveData() = currentTimeMutableLiveData

    fun preparePlayer() {
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateMutableLiveData.postValue(PlayerState.PREPARED)
            startPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            playerStateMutableLiveData.postValue(PlayerState.PREPARED)
            resetTimer()
        }
    }


    fun buttonPlayClicked() {
        when (playerStateMutableLiveData.value) {
            PlayerState.PLAYING -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateMutableLiveData.postValue(PlayerState.PLAYING)
        handler.postDelayed(currentTimeRunnable, GET_CURRENT_TIME_DELAY)
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateMutableLiveData.postValue(PlayerState.PAUSED)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(currentTimeRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(currentTimeRunnable)
        currentTimeMutableLiveData.postValue("00:00")
    }

    fun onPause() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }
}
