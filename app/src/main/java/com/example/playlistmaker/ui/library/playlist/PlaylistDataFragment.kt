package com.example.playlistmaker.ui.library.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.ui.library.playlist.compose.playlist_data_screen.PlaylistDataScreen
import com.example.playlistmaker.ui.theme.ColorTheme


class PlaylistDataFragment : Fragment() {
    private val args by navArgs<PlaylistDataFragmentArgs>()

    private val playlist by lazy { args.playlist }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ColorTheme {
                    PlaylistDataScreen(playlist = playlist,
                        onSongClicked = { song ->
                        findNavController().navigate(
                            PlaylistDataFragmentDirections.actionPlaylistDataFragmentToPlayerFragment(
                                song
                            )
                        )
                    },
                        onEditPlaylistClicked = {playlist->
                            findNavController().navigate(PlaylistDataFragmentDirections.actionPlaylistDataFragmentToEditPlaylistFragment(playlist))
                        }
                        )
                }
            }
        }
    }
}