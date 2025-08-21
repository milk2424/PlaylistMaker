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
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import java.io.IOException

class PlayerViewModel(private val songUrl: String, private val mediaPlayer: MediaPlayer) :
    ViewModel() {
    companion object {
        fun getFactory(songUrl: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val mediaPlayer = Creator.provideMediaPlayer()
                if (songUrl != null) PlayerViewModel(
                    songUrl, mediaPlayer
                ) else throw IOException("songUrl is null")
            }
        }

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
