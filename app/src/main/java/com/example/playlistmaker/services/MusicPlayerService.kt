package com.example.playlistmaker.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.playlistmaker.domain.player.repository.MusicPlayer
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicPlayerService(private val mediaPlayer: MediaPlayer) : Service(), MusicPlayer {

    private val binder = MusicPlayerServiceBinder()

    private var currentSong: MusicPlayerServiceSongData? = null

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerStateFlow = _playerStateFlow.asStateFlow()

    private val currentTimeScope = CoroutineScope(Dispatchers.Default)

    private var observeCurrentTimeJob: Job? = null

    override fun onBind(intent: Intent): IBinder? {
        currentSong = MusicPlayerServiceSongData(
            intent.getStringExtra(SONG_URL)!!,
            intent.getStringExtra(SONG_NAME)!!,
            intent.getStringExtra(SONG_ARTIST)!!
        )
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        preparePlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun preparePlayer() {
        if (currentSong?.url?.isEmpty() != false) return
        mediaPlayer.setDataSource(currentSong?.url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerStateFlow.value = PlayerState.Prepared()
        }
        mediaPlayer.setOnCompletionListener {
            _playerStateFlow.value = PlayerState.Prepared()
        }
    }

    private fun releasePlayer() {
        currentTimeScope.cancel()
        mediaPlayer.stop()
        _playerStateFlow.value = PlayerState.Default()
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnCompletionListener(null)
        mediaPlayer.release()
    }

    override fun start() {
        mediaPlayer.start()
        _playerStateFlow.value = PlayerState.Playing(getCurrentTimeMapped())
        observeCurrentTime()
    }

    override fun pause() {
        _playerStateFlow.value = PlayerState.Paused(getCurrentTimeMapped())
        observeCurrentTimeJob?.cancel()
        mediaPlayer.pause()
    }


    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerStateFlow
    }

    private fun getCurrentTimeMapped() = PlayerTimeMapper.map(mediaPlayer.currentPosition)

    private fun observeCurrentTime() {
        observeCurrentTimeJob?.cancel()
        observeCurrentTimeJob = currentTimeScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(GET_CURRENT_TIME_DELAY)
                if (mediaPlayer.isPlaying) _playerStateFlow.value =
                    PlayerState.Playing(getCurrentTimeMapped())
            }
        }
    }

    inner class MusicPlayerServiceBinder : Binder() {
        fun getMusicPlayerService(): MusicPlayer {
            return this@MusicPlayerService
        }
    }

    companion object {
        private const val SONG_URL = "song_url"
        private const val SONG_NAME = "song_name"
        private const val SONG_ARTIST = "song_artist"
        private const val GET_CURRENT_TIME_DELAY = 300L
    }

}