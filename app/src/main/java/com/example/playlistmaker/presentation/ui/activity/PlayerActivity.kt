package com.example.playlistmaker.presentation.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.entity.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerDpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerImageMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.ui.PlayerState.PAUSED
import com.example.playlistmaker.presentation.ui.PlayerState.PLAYING
import com.example.playlistmaker.presentation.ui.PlayerState.PREPARING
import com.example.playlistmaker.presentation.ui.PlayerState.STOPPED
import com.example.playlistmaker.presentation.ui.activity.SearchActivity.Companion.PLAY_TRACK

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var currentTrack: Song? = null
    private val player by lazy { MediaPlayer() }
    private var playerState = STOPPED
    private var isButtonPlayClicked = false
    private var isTrackStoppedBeforeOnPause = false

    private val timeControlRunnable = object : Runnable {
        override fun run() {
            binding.currentSongTime.text = PlayerTimeMapper.map(player.currentPosition)
            mainHandler.postDelayed(this, UPDATE_CURRENT_TIME_DELAY)
        }
    }
    private val mainHandler by lazy(mode = LazyThreadSafetyMode.NONE) { Handler(mainLooper) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTrack = IntentCompat.getParcelableExtra(intent, PLAY_TRACK, Song::class.java)

        binding.songName.text = currentTrack?.trackName

        binding.songArtist.text = currentTrack?.artistName

        binding.categoryDurationValue.text = PlayerTimeMapper.map(currentTrack!!.trackTimeMillis)

        if (currentTrack?.collectionName.isNullOrEmpty()) {
            binding.albumGroup.visibility = GONE
        } else {
            binding.categoryAlbumValue.text = currentTrack?.collectionName
        }

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            binding.yearGroup.visibility = GONE
        } else {
            binding.categoryYearValue.text = currentTrack?.releaseDate!!.substringBefore('-')
        }

        binding.categoryGenreValue.text = currentTrack?.primaryGenreName

        binding.categoryCountryValue.text = currentTrack?.country

        Glide.with(binding.songImage).load(PlayerImageMapper.map(currentTrack!!.artworkUrl100))
            .transform(
                RoundedCorners(
                    PlayerDpToPxMapper.map(8f, this)
                )
            ).placeholder(R.drawable.player_no_track_image).into(binding.songImage)

        binding.arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        player.setDataSource(currentTrack?.previewUrl)
        player.prepare()
        playerState = PREPARING
        player.setOnCompletionListener {
            pausePlayer()
            binding.currentSongTime.setText(R.string.start_track_time)
        }
        binding.btnPlay.setOnClickListener {
            if (!debouncePlayButton()) {
                playerControl()
                isTrackStoppedBeforeOnPause = !isTrackStoppedBeforeOnPause
            }
        }

    }

    private fun startPlayer() {
        player.start()
        playerState = PLAYING
        binding.btnPlay.setImageResource(R.drawable.btn_pause_player)
        mainHandler.post(timeControlRunnable)
    }

    private fun pausePlayer() {
        player.pause()
        playerState = PAUSED
        binding.btnPlay.setImageResource(R.drawable.btn_start_player)
        mainHandler.removeCallbacks(timeControlRunnable)
    }


    private fun playerControl() {
        when (playerState) {
            PLAYING -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun debouncePlayButton(): Boolean {
        val currentIsButtonPlayClicked = isButtonPlayClicked
        if (!currentIsButtonPlayClicked) {
            isButtonPlayClicked = true
            mainHandler.postDelayed({ isButtonPlayClicked = false }, BUTTON_PLAY_DELAY)
        }
        return currentIsButtonPlayClicked
    }

    override fun onResume() {
        super.onResume()
        if (!isTrackStoppedBeforeOnPause) startPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    companion object {
        private const val BUTTON_PLAY_DELAY = 200L
        private const val UPDATE_CURRENT_TIME_DELAY = 300L
    }
}