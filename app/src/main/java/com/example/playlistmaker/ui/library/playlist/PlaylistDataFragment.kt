package com.example.playlistmaker.ui.library.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDataBinding
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistDataViewModel
import com.example.playlistmaker.ui.FragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistDataFragment : FragmentBinding<FragmentPlaylistDataBinding>() {

    private val args by navArgs<PlaylistDataFragmentArgs>()

    private val playlist by lazy { args.playlist }

    private val viewModel: PlaylistDataViewModel by viewModel()

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlaylistDataBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            playlist.apply {
                tvPlaylistName.text = name
                tvPlaylistSongsCount.text = requireContext().resources.getQuantityString(
                    R.plurals.tracks_plurals,
                    songsCount,
                    songsCount
                )
                tvPlaylistDescription.text = description
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.playlistTime.collect { minutes ->
                    binding.tvPlaylistTime.text = requireContext().resources.getQuantityString(
                        R.plurals.minute_plurals,
                        minutes,
                        minutes
                    )
                }
            }
        }
    }

}