package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerDpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerImageMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import com.example.playlistmaker.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.ui.FragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : FragmentBinding<FragmentPlayerBinding>() {
    private var currentTrack: Song? = null
    private var isButtonPlayClicked = false
    private var isButtonSaveToFavouriteClicked = false

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(currentTrack)
    }

    private val mainHandler by lazy(mode = LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    private val args by navArgs<PlayerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentTrack = args.song

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
                    PlayerDpToPxMapper.map(8f, requireContext())
                )
            ).placeholder(R.drawable.player_no_track_image).into(binding.songImage)

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPlay.setOnClickListener {
            if (!debouncePlayButton()) {
                viewModel.buttonPlayClicked()
            }
        }

        lifecycleScope.launch {
            viewModel.isSongFavouriteState.collect { state ->
                renderIsSongFavouriteState(state)
            }
        }


        binding.btnSaveToFavorite.setOnClickListener {
            if (!debounceSaveToFavouriteButton())
                viewModel.switchIsSongFavouriteState()
        }

        viewModel.playerStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.currentSongTime.text = state.time
            when (state) {
                is PlayerState.Playing, is PlayerState.Default -> binding.btnPlay.setImageResource(R.drawable.btn_pause_player)
                is PlayerState.Paused, is PlayerState.Prepared -> binding.btnPlay.setImageResource(R.drawable.btn_start_player)
            }
        }
    }

    private fun debouncePlayButton(): Boolean {
        val currentIsButtonPlayClicked = isButtonPlayClicked
        if (!currentIsButtonPlayClicked) {
            isButtonPlayClicked = true
            mainHandler.postDelayed({ isButtonPlayClicked = false }, BUTTONS_DELAY)
        }
        return currentIsButtonPlayClicked
    }


    private fun debounceSaveToFavouriteButton(): Boolean {
        val currentSaveToFavouriteButtonClicked = isButtonSaveToFavouriteClicked
        if (!currentSaveToFavouriteButtonClicked) {
            isButtonSaveToFavouriteClicked = true
            mainHandler.postDelayed({ isButtonSaveToFavouriteClicked = false }, BUTTONS_DELAY)
        }
        return currentSaveToFavouriteButtonClicked
    }

    private fun renderIsSongFavouriteState(state: Boolean) {
        val tint =
            if (state) R.drawable.btn_save_to_favourite_active else R.drawable.btn_save_to_favorite_inactive
        Glide.with(requireContext()).load(tint).into(binding.btnSaveToFavorite)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(layoutInflater, container, false)

    companion object {
        private const val BUTTONS_DELAY = 200L
    }
}