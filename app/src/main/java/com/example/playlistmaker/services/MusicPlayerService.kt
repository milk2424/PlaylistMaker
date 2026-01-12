package com.example.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
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

class MusicPlayerService() : Service(), MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null

    private val binder = MusicPlayerServiceBinder()

    private var currentSongUrl: String? = null

    private var currentSongForegroundData: MusicPlayerServiceSongData? = null

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerStateFlow = _playerStateFlow.asStateFlow()

    private val currentTimeScope = CoroutineScope(Dispatchers.Default)

    private var observeCurrentTimeJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder? {
        currentSongUrl = intent.getStringExtra(SONG_URL)!!
        preparePlayer()
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        currentSongForegroundData = MusicPlayerServiceSongData(
            intent.getStringExtra(SONG_URL)!!,
            intent.getStringExtra(SONG_NAME)!!,
            intent.getStringExtra(SONG_ARTIST)!!
        )
        showNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            100,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun removeNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun preparePlayer() {
        if (currentSongUrl?.isEmpty() != false) return
        mediaPlayer?.setDataSource(currentSongUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerStateFlow.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            removeNotification()
            _playerStateFlow.value = PlayerState.Prepared()
        }
    }

    private fun releasePlayer() {
        currentTimeScope.cancel()
        mediaPlayer?.stop()
        _playerStateFlow.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun start() {
        mediaPlayer?.start()
        _playerStateFlow.value = PlayerState.Playing(getCurrentTimeMapped())
        observeCurrentTime()
    }

    override fun pause() {
        _playerStateFlow.value = PlayerState.Paused(getCurrentTimeMapped())
        observeCurrentTimeJob?.cancel()
        mediaPlayer?.pause()
    }

    override fun stop() {
        releasePlayer()
        stopSelf()
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerStateFlow
    }

    private fun getCurrentTimeMapped() = PlayerTimeMapper.map(mediaPlayer!!.currentPosition)

    private fun observeCurrentTime() {
        observeCurrentTimeJob?.cancel()
        observeCurrentTimeJob = currentTimeScope.launch {
            while (mediaPlayer!!.isPlaying) {
                delay(GET_CURRENT_TIME_DELAY)
                if (mediaPlayer?.isPlaying ?: false) _playerStateFlow.value =
                    PlayerState.Playing(getCurrentTimeMapped())
            }
        }
    }

    inner class MusicPlayerServiceBinder : Binder() {
        fun getMusicPlayerService(): MusicPlayer {
            return this@MusicPlayerService
        }
    }

    private fun createNotificationChannel() {

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationChannel.description = NOTIFICATION_CHANNEL_NAME
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createNotification() = Notification
        .Builder(this, NOTIFICATION_CHANNEL_ID)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(mapSongForegroundData())
        .setSmallIcon(R.mipmap.ic_launcher)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .build()

    private fun mapSongForegroundData() =
        "${currentSongForegroundData?.songArtist} - ${currentSongForegroundData?.songName}"

    companion object {
        private const val SONG_URL = "song_url"
        private const val SONG_NAME = "song_name"
        private const val SONG_ARTIST = "song_artist"
        private const val NOTIFICATION_CHANNEL_ID = "music_foreground_service"
        private const val NOTIFICATION_CHANNEL_NAME = "Music service"
        private const val GET_CURRENT_TIME_DELAY = 300L
        private const val NOTIFICATION_TITLE = "Playlist Maker"
    }

}