package com.example.playlistmaker.ui.library.favourite_songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.ui.library.LibraryFragmentDirections
import com.example.playlistmaker.ui.library.compose.pager.screens.favourite_songs.FavouriteSongsScreen
import com.example.playlistmaker.ui.search.SearchFragmentDirections
import com.example.playlistmaker.ui.theme.ColorTheme


class FavouriteSongsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ColorTheme {
                    FavouriteSongsScreen(){song->
                        findNavController().navigate(
                            LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(
                                song
                            )
                        )
                    }
                }
            }
        }
    }

//    private val viewModel: FavouriteSongsViewModel by viewModel()
//
//    private val adapter = SongAdapter()
//
//    private var onItemClick: ((Song) -> Unit)? = null
//
//    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
//        FragmentFavouriteSongsBinding.inflate(layoutInflater, container, false)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.favouriteSongsRCView.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        onItemClick = debounce(
//            TRACK_ITEM_CLICKED_DELAY,
//            viewLifecycleOwner.lifecycleScope,
//            false
//        ) { song ->
//            findNavController().navigate(
//                LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(
//                    song
//                )
//            )
//        }
//
//        adapter.onClickCallback = onItemClick
//
//        binding.favouriteSongsRCView.adapter = adapter
//
//        viewModel.loadFavouriteSongs()
//
//        viewModel.observeFavouriteSongsState().observe(viewLifecycleOwner) { state ->
//            renderState(state)
//        }
//    }
//
//    private fun renderState(state: FavouriteSongsState) {
//        when (state) {
//            is FavouriteSongsState.Data -> {
//                showContent(state.songs)
//            }
//
//            is FavouriteSongsState.Empty -> {
//                showEmpty()
//            }
//        }
//    }
//
//    private fun showContent(songs: List<Song>) {
//        adapter.resetAdapter(songs)
//        binding.errorEmptyLibraryLayout.visibility = GONE
//        binding.favouriteSongsRCView.visibility = VISIBLE
//    }
//
//    private fun showEmpty() {
//        binding.errorEmptyLibraryLayout.visibility = VISIBLE
//        binding.favouriteSongsRCView.visibility = GONE
//    }
//
//    companion object {
//        fun newInstance() = FavouriteSongsFragment()
//        private const val TRACK_ITEM_CLICKED_DELAY = 500L
//    }

}