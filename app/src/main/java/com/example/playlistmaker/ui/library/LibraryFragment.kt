package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.FragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : FragmentBinding<FragmentLibraryBinding>() {

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentLibraryBinding.inflate(layoutInflater, container, false)

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favourite_songs)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}