package com.example.playlistmaker.ui.library.favourite_songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavouriteSongsBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.favourite_songs.FavouriteSongsState
import com.example.playlistmaker.presentation.view_model.FavouriteSongsViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.library.LibraryFragmentDirections
import debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouriteSongsFragment : FragmentBinding<FragmentFavouriteSongsBinding>() {

    private val viewModel: FavouriteSongsViewModel by viewModel()

    private val adapter = FavouriteSongsAdapter()

    private var onItemClick: ((Song) -> Unit)? = null

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavouriteSongsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favouriteSongsRCView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.favouriteSongsRCView.adapter = adapter


        onItemClick = debounce(
            TRACK_ITEM_CLICKED_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { song ->
            viewModel.addSongToHistory(song)
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(
                    song
                )
            )
        }


        viewModel.loadFavouriteSongs()

        lifecycleScope.launch {
            viewModel.favouriteSongsState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: FavouriteSongsState) {
        when (state) {
            is FavouriteSongsState.Data -> {
                showContent(state.songs)
            }

            is FavouriteSongsState.Empty -> {
                showEmpty()
            }

            is FavouriteSongsState.Loading -> {

            }
        }
    }

    private fun showContent(songs: List<Song>) {
        binding.errorEmptyLibraryLayout.visibility = GONE
        binding.favouriteSongsRCView.visibility = VISIBLE
        adapter.favouriteSongs = songs
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.errorEmptyLibraryLayout.visibility = VISIBLE
        binding.favouriteSongsRCView.visibility = GONE
    }

    companion object {
        fun newInstance() = FavouriteSongsFragment()
        private const val TRACK_ITEM_CLICKED_DELAY = 500L
    }

}