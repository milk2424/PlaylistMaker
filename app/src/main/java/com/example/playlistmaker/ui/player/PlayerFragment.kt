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
                    PlayerScreen(song = currentSong!!, viewModel = viewModel, buttonBackClicked = {
                        findNavController().popBackStack()
                    }
                    ) {
                        findNavController().navigate(
                            PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment()
                        )
                    }
                }
            }
        }
    }


//
//    val connectionReceiver = ConnectionReceiver()
//
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
//
//
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