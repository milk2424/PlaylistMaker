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
                    LibraryFragmentCompose() { song ->
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

//    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
//        FragmentLibraryBinding.inflate(layoutInflater, container, false)
//
//    private lateinit var tabLayoutMediator: TabLayoutMediator
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.viewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
//        tabLayoutMediator =
//            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//                when (position) {
//                    0 -> tab.text = getString(R.string.favourite_songs)
//                    1 -> tab.text = getString(R.string.playlists)
//                }
//            }
//        tabLayoutMediator.attach()
//    }
//
//    override fun onDestroyView() {
//        binding.viewPager.adapter = null
//        tabLayoutMediator.detach()
//        super.onDestroyView()
//    }
}