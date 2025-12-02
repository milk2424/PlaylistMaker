package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.DpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerImageMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.PlayerState
import com.example.playlistmaker.presentation.utils.player.BottomSheetUIState
import com.example.playlistmaker.presentation.view_model.player.PlayerViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.player.adapter.PlayerPlaylistAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.suspendCoroutine
import kotlin.text.Typography.section

class PlayerFragment : FragmentBinding<FragmentPlayerBinding>() {
    private var currentSong: Song? = null
    private var isButtonPlayClicked = false
    private var isButtonSaveToFavouriteClicked = false

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(currentSong)
    }

    private val mainHandler by lazy(mode = LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    private val args by navArgs<PlayerFragmentArgs>()

    private val playlistAdapter = PlayerPlaylistAdapter { playlist ->
        viewModel.addSongToPlaylist(currentSong!!, playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentSong = args.song

        binding.songName.text = currentSong?.trackName

        binding.songArtist.text = currentSong?.artistName

        binding.categoryDurationValue.text = PlayerTimeMapper.map(currentSong!!.trackTimeMillis)

        if (currentSong?.collectionName.isNullOrEmpty()) {
            binding.albumGroup.visibility = GONE
        } else {
            binding.categoryAlbumValue.text = currentSong?.collectionName
        }

        if (currentSong?.releaseDate.isNullOrEmpty()) {
            binding.yearGroup.visibility = GONE
        } else {
            binding.categoryYearValue.text = currentSong?.releaseDate!!.substringBefore('-')
        }

        binding.categoryGenreValue.text = currentSong?.primaryGenreName

        binding.categoryCountryValue.text = currentSong?.country

        Glide.with(binding.songImage).load(PlayerImageMapper.map(currentSong!!.artworkUrl100))
            .transform(
                RoundedCorners(
                    DpToPxMapper.map(8f, requireContext())
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

        binding.btnSaveToFavorite.setOnClickListener {
            if (!debounceSaveToFavouriteButton())
                viewModel.switchIsSongFavouriteState()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = STATE_HIDDEN
            binding.overlay.apply {
                visibility = VISIBLE
                alpha = 0f
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    STATE_COLLAPSED -> {
                        viewModel.loadPlaylists()
                    }

                    STATE_HIDDEN -> {
                        viewModel.resetPlaylistsData()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1f) / 2
            }

        })

        binding.rvPlaylists.adapter = playlistAdapter
        binding.rvPlaylists.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )


        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.isSongFavouriteState.collect { state ->
                    renderIsSongFavouriteState(state)
                }
            }
            launch {
                viewModel.isSongAddedToPlaylist.collect { state ->
                    if (state.second) bottomSheetBehavior.state = STATE_HIDDEN
                    showAddMessage(state)
                }
            }
            launch {
                viewModel.bottomSheetDataState.collect { state ->
                    when (state) {
                        is BottomSheetUIState.Data -> {
                            playlistAdapter.playlists = state.playlists
                            playlistAdapter.notifyDataSetChanged()
                        }

                        is BottomSheetUIState.Default -> {
                            playlistAdapter.playlists = emptyList()
                            playlistAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        binding.btnAddToLibrary.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }

        viewModel.playerStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.currentSongTime.text = state.time
            when (state) {
                is PlayerState.Playing -> binding.btnPlay.setImageResource(R.drawable.btn_pause_player)
                is PlayerState.Paused, is PlayerState.Prepared, is PlayerState.Default -> binding.btnPlay.setImageResource(R.drawable.btn_start_player)
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

    @SuppressLint("RestrictedApi")
    private fun showAddMessage(state: Pair<String, Boolean>) {
        val message =
            if (state.second) requireContext().getString(R.string.song_added_to_playlist) else
                requireContext().getString(R.string.song_is_already_in_playlist)

        val messageFormatted = String.format(message, state.first)

        val parent = requireActivity().findViewById<ViewGroup>(R.id.mainContainerView)

        val snackBar = Snackbar.make(parent, "", Snackbar.LENGTH_SHORT)

        val snackbarView = LayoutInflater.from(requireActivity()).inflate(
            R.layout.snackbar_new_playlist,
            parent,
            false
        )

        snackbarView.findViewById<TextView>(R.id.tvText).text = messageFormatted

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(snackbarView)

        snackBar.show()
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