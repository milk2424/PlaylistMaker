package com.example.playlistmaker.ui.library.favourite_songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavouriteSongsBinding
import com.example.playlistmaker.utils.FragmentBinding


class FavouriteSongsFragment : FragmentBinding<FragmentFavouriteSongsBinding>() {

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavouriteSongsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() = FavouriteSongsFragment()
    }

}