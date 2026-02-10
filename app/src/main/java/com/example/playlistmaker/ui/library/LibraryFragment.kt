package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.ui.library.compose.LibraryFragmentCompose
import com.example.playlistmaker.ui.theme.ColorTheme

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ColorTheme {
                    LibraryFragmentCompose(
                        onSongClicked = { song ->
                            findNavController().navigate(
                                LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(
                                    song
                                )
                            )
                        },
                        onPlaylistClicked = { playlist ->
                            findNavController().navigate(
                                LibraryFragmentDirections.actionLibraryFragmentToPlaylistDataFragment(
                                    playlist
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}