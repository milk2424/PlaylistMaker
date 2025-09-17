package com.example.playlistmaker.ui.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.library.favourite_songs.FavouriteSongsFragment
import com.example.playlistmaker.ui.library.playlist.PlaylistFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavouriteSongsFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
}