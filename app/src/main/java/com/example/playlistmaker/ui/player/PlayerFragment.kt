package com.example.playlistmaker.ui.player

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.broadcast_receiver.ConnectionReceiver
import com.example.playlistmaker.presentation.mapper.player_mapper.DpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerImageMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper
import com.example.playlistmaker.presentation.utils.player.BottomSheetUIState
import com.example.playlistmaker.presentation.utils.player.PlayerState
import com.example.playlistmaker.presentation.view_model.player.PlayerViewModel
import com.example.playlistmaker.services.MusicPlayerService
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.player.adapter.PlayerPlaylistAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : FragmentBinding<FragmentPlayerBinding>() {
    private var currentSong: Song? = null
    private var isButtonPlayClicked = false
    private var isButtonSaveToFavouriteClicked = false

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(currentSong)
    }

    val connectionReceiver = ConnectionReceiver()

    private val mainHandler by lazy(mode = LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    private val args by navArgs<PlayerFragmentArgs>()

    private val playlistAdapter = PlayerPlaylistAdapter { playlist ->
        viewModel.addSongToPlaylist(currentSong!!, playlist)
    }

    private val musicServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicPlayerService.MusicPlayerServiceBinder
            viewModel.setupMusicPlayer(binder.getMusicPlayerService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeMusicPlayer()
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted && viewModel.needToStartForegroundService()) {
                startMusicPlayerService()
            }
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
            if (!debounceSaveToFavouriteButton()) viewModel.switchIsSongFavouriteState()
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
            requireContext(), LinearLayoutManager.VERTICAL, false
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
                is PlayerState.Playing -> binding.btnPlay.isPlaying = true
                is PlayerState.Paused, is PlayerState.Prepared, is PlayerState.Default -> binding.btnPlay.isPlaying =
                    false
            }
        }

        checkNotificationPermission()

        bindMusicPlayerService()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun bindMusicPlayerService() {
        val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
            putExtra(SONG_URL, currentSong?.previewUrl)
        }
        requireContext().bindService(intent, musicServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicPlayerService() {
        requireContext().unbindService(musicServiceConnection)
    }

    private fun startMusicPlayerService() {
        val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
            putExtra(SONG_URL, currentSong?.previewUrl)
            putExtra(SONG_NAME, currentSong?.trackName)
            putExtra(SONG_ARTIST, currentSong?.artistName)
        }
        ContextCompat.startForegroundService(requireContext(), intent)
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
            if (state.second) requireContext().getString(R.string.song_added_to_playlist) else requireContext().getString(
                R.string.song_is_already_in_playlist
            )

        val messageFormatted = String.format(message, state.first)

        val parent = requireActivity().findViewById<ViewGroup>(R.id.mainContainerView)

        val snackBar = Snackbar.make(parent, "", Snackbar.LENGTH_SHORT)

        val snackbarView = LayoutInflater.from(requireActivity()).inflate(
            R.layout.snackbar_new_playlist, parent, false
        )

        snackbarView.findViewById<TextView>(R.id.tvText).text = messageFormatted

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(snackbarView)

        snackBar.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.removeNotification()
        requireActivity().registerReceiver(
            connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {

        if (isRemoving) viewModel.removeMusicPlayer()
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                startMusicPlayerService()
            }
        requireActivity().unregisterReceiver(connectionReceiver)
        super.onPause()
    }


    override fun onDestroyView() {
        unbindMusicPlayerService()
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewModel.removeMusicPlayer()
        super.onDestroy()
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlayerBinding.inflate(layoutInflater, container, false)

    companion object {
        private const val BUTTONS_DELAY = 200L
        private const val SONG_URL = "song_url"
        private const val SONG_NAME = "song_name"
        private const val SONG_ARTIST = "song_artist"
    }
}