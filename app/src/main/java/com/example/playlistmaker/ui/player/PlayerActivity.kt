package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerDpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerImageMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import com.example.playlistmaker.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.SearchActivity.Companion.PLAY_TRACK

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var currentTrack: Song? = null
    private var isButtonPlayClicked = false

    private val viewModel by lazy {
        ViewModelProvider(
            this, PlayerViewModel.getFactory(currentTrack?.previewUrl)
        ).get(PlayerViewModel::class.java)
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

        binding.btnPlay.setOnClickListener {
            if (!debouncePlayButton()) {
                viewModel.buttonPlayClicked()
            }
        }

        viewModel.currentTimeLiveData().observe(this) { time ->
            binding.currentSongTime.text = time
        }

        viewModel.playerStateLiveData().observe(this) { state ->
            when (state) {
                PlayerState.PLAYING, PlayerState.DEFAULT -> binding.btnPlay.setImageResource(R.drawable.btn_pause_player)
                PlayerState.PAUSED, PlayerState.PREPARED -> binding.btnPlay.setImageResource(R.drawable.btn_start_player)
            }
        }

        viewModel.preparePlayer()


    }

    private fun debouncePlayButton(): Boolean {
        val currentIsButtonPlayClicked = isButtonPlayClicked
        if (!currentIsButtonPlayClicked) {
            isButtonPlayClicked = true
            mainHandler.postDelayed({ isButtonPlayClicked = false }, BUTTON_PLAY_DELAY)
        }
        return currentIsButtonPlayClicked
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        private const val BUTTON_PLAY_DELAY = 200L
    }
}