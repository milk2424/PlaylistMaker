package com.example.playlistmaker.ui.library.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.library.LibraryFragmentDirections
import com.example.playlistmaker.ui.library.playlist.adapter.PlaylistAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : FragmentBinding<FragmentPlaylistBinding>() {

    private val viewModel: PlaylistViewModel by viewModel()

    private val playlistAdapter = PlaylistAdapter { playlist ->
        findNavController().navigate(
            LibraryFragmentDirections.actionLibraryFragmentToPlaylistDataFragment(
                playlist
            )
        )
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlaylistBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
        binding.rvPlaylists.adapter = playlistAdapter
        binding.rvPlaylists.layoutManager = GridLayoutManager(
            requireContext(), 2,
            GridLayoutManager.VERTICAL, false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderState(state)
            }
        }

        viewModel.loadPlaylists()

    }

    private fun renderState(state: PlaylistUIState) {
        when (state) {
            is PlaylistUIState.Empty -> showEmpty()
            is PlaylistUIState.Loading -> showLoading()
            is PlaylistUIState.Success -> showData(state.data)
        }
    }

    private fun showEmpty() {
        binding.errorEmptyLibraryLayout.visibility = VISIBLE
        binding.rvPlaylists.visibility = GONE
    }

    private fun showLoading() {
        binding.errorEmptyLibraryLayout.visibility = GONE
        binding.rvPlaylists.visibility = GONE
    }

    private fun showData(data: List<Playlist>) {
        playlistAdapter.playlists = data
        Log.i("TAG", "${data}: ")
        playlistAdapter.notifyDataSetChanged()
        binding.errorEmptyLibraryLayout.visibility = GONE
        binding.rvPlaylists.visibility = VISIBLE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}