package com.example.playlistmaker.ui.library.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.utils.FragmentBinding


class PlaylistFragment : FragmentBinding<FragmentPlaylistBinding>() {


    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlaylistBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}