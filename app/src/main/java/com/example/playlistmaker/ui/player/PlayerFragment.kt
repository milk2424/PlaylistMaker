package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.view_model.player.PlayerViewModel
import com.example.playlistmaker.ui.player.compose.PlayerScreen
import com.example.playlistmaker.ui.theme.ColorTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerFragment : Fragment() {

    private val args by navArgs<PlayerFragmentArgs>()
    private var currentSong: Song? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentSong = args.song
        val viewModel: PlayerViewModel by viewModel {
            parametersOf(currentSong)
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ColorTheme {
                    PlayerScreen(song = currentSong!!, viewModel = viewModel) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }


//    private var isButtonPlayClicked = false
//    private var isButtonSaveToFavouriteClicked = false

//
//    val connectionReceiver = ConnectionReceiver()
//
//    private val musicServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as MusicPlayerService.MusicPlayerServiceBinder
//            viewModel.setupMusicPlayer(binder.getMusicPlayerService())
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            viewModel.removeMusicPlayer()
//        }
//    }
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted && viewModel.needToStartForegroundService()) {
//                startMusicPlayerService()
//            }
//        }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
//            state = STATE_HIDDEN
//            binding.overlay.apply {
//                visibility = VISIBLE
//                alpha = 0f
//            }
//        }
//
//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            @SuppressLint("SwitchIntDef")
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//
//                    STATE_COLLAPSED -> {
//                        viewModel.loadPlaylists()
//                    }
//
//                    STATE_HIDDEN -> {
//                        viewModel.resetPlaylistsData()
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                binding.overlay.alpha = (slideOffset + 1f) / 2
//            }
//
//        })
//
//        binding.rvPlaylists.adapter = playlistAdapter
//        binding.rvPlaylists.layoutManager = LinearLayoutManager(
//            requireContext(), LinearLayoutManager.VERTICAL, false
//        )
//
//        viewLifecycleOwner.lifecycleScope.apply {
//            }
//            launch {
//                viewModel.isSongAddedToPlaylist.collect { state ->
//                    if (state.second) bottomSheetBehavior.state = STATE_HIDDEN
//                    showAddMessage(state)
//                }
//            }
//            launch {
//                viewModel.bottomSheetDataState.collect { state ->
//                    when (state) {
//                        is BottomSheetUIState.Data -> {
//                            playlistAdapter.playlists = state.playlists
//                            playlistAdapter.notifyDataSetChanged()
//                        }
//
//                        is BottomSheetUIState.Default -> {
//                            playlistAdapter.playlists = emptyList()
//                            playlistAdapter.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//        }
//
//        binding.btnNewPlaylist.setOnClickListener {
//            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
//        }
//
//        binding.btnAddToLibrary.setOnClickListener {
//            bottomSheetBehavior.state = STATE_COLLAPSED
//        }
//
//        viewModel.playerStateLiveData().observe(viewLifecycleOwner) { state ->
//            binding.currentSongTime.text = state.time
//            when (state) {
//                is PlayerState.Playing -> binding.btnPlay.isPlaying = true
//                is PlayerState.Paused, is PlayerState.Prepared, is PlayerState.Default -> binding.btnPlay.isPlaying =
//                    false
//            }
//        }
//
//        launchNotificationPermission()
//
//        bindMusicPlayerService()
//    }
//
//    private fun launchNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//        }
//    }
//
//    private fun checkNotificationPermission() =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED else true
//
//    private fun bindMusicPlayerService() {
//        val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
//            putExtra(SONG_URL, currentSong?.previewUrl)
//        }
//        requireContext().bindService(intent, musicServiceConnection, Context.BIND_AUTO_CREATE)
//    }
//
//    private fun unbindMusicPlayerService() {
//        requireContext().unbindService(musicServiceConnection)
//    }
//
//    private fun startMusicPlayerService() {
//        val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
//            putExtra(SONG_URL, currentSong?.previewUrl)
//            putExtra(SONG_NAME, currentSong?.trackName)
//            putExtra(SONG_ARTIST, currentSong?.artistName)
//        }
//        ContextCompat.startForegroundService(requireContext(), intent)
//    }
//
//    private fun debouncePlayButton(): Boolean {
//        val currentIsButtonPlayClicked = isButtonPlayClicked
//        if (!currentIsButtonPlayClicked) {
//            isButtonPlayClicked = true
//            mainHandler.postDelayed({ isButtonPlayClicked = false }, BUTTONS_DELAY)
//        }
//        return currentIsButtonPlayClicked
//    }
//
//
//    private fun debounceSaveToFavouriteButton(): Boolean {
//        val currentSaveToFavouriteButtonClicked = isButtonSaveToFavouriteClicked
//        if (!currentSaveToFavouriteButtonClicked) {
//            isButtonSaveToFavouriteClicked = true
//            mainHandler.postDelayed({ isButtonSaveToFavouriteClicked = false }, BUTTONS_DELAY)
//        }
//        return currentSaveToFavouriteButtonClicked
//    }
//
//
//    @SuppressLint("RestrictedApi")
//    private fun showAddMessage(state: Pair<String, Boolean>) {
//        val message =
//            if (state.second) requireContext().getString(R.string.song_added_to_playlist) else requireContext().getString(
//                R.string.song_is_already_in_playlist
//            )
//
//        val messageFormatted = String.format(message, state.first)
//
//        val parent = requireActivity().findViewById<ViewGroup>(R.id.mainContainerView)
//
//        val snackBar = Snackbar.make(parent, "", Snackbar.LENGTH_SHORT)
//
//        val snackbarView = LayoutInflater.from(requireActivity()).inflate(
//            R.layout.snackbar_new_playlist, parent, false
//        )
//
//        snackbarView.findViewById<TextView>(R.id.tvText).text = messageFormatted
//
//        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
//        snackbarLayout.setPadding(0, 0, 0, 0)
//        snackbarLayout.addView(snackbarView)
//
//        snackBar.show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewModel.removeNotification()
//        requireActivity().registerReceiver(
//            connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        )
//    }
//
//    override fun onPause() {
//
//        if (isRemoving) viewModel.removeMusicPlayer()
//        else
//            if (checkNotificationPermission()) startMusicPlayerService()
//        requireActivity().unregisterReceiver(connectionReceiver)
//        super.onPause()
//    }
//
//
//    override fun onDestroyView() {
//        unbindMusicPlayerService()
//        super.onDestroyView()
//    }
//
//    override fun onDestroy() {
//        viewModel.removeMusicPlayer()
//        super.onDestroy()
//    }
//
//    companion object {
//        private const val BUTTONS_DELAY = 200L
//        private const val SONG_URL = "song_url"
//        private const val SONG_NAME = "song_name"
//        private const val SONG_ARTIST = "song_artist"
//    }
}