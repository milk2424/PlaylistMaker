package com.example.playlistmaker.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.theme.ColorTheme

class SearchFragment : Fragment()/*FragmentBinding<FragmentSearchBinding>()*/ {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ColorTheme {
                    SearchFragmentCompose()
                }
            }
        }
    }

//
//    private lateinit var onItemClick: (Song) -> Unit
//
//    val connectionReceiver = ConnectionReceiver()
//
//    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
//        FragmentSearchBinding.inflate(layoutInflater, container, false)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.trackRcView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.trackRcView.adapter = songAdapter
//
//
//        onItemClick = debounce(
//            TRACK_ITEM_CLICKED_DELAY,
//            viewLifecycleOwner.lifecycleScope,
//            false
//        ) { song ->
//            viewModel.addSongToHistory(song)
//            findNavController().navigate(
//                SearchFragmentDirections.actionSearchFragmentToPlayerFragment(
//                    song
//                )
//            )
//        }

//        songAdapter.onClickCallback = { song ->
//            onItemClick(song)
//        }
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        requireActivity().registerReceiver(
//            connectionReceiver,
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        )
//    }
//
//    override fun onPause() {
//        requireActivity().unregisterReceiver(connectionReceiver)
//        super.onPause()
//    }
//

//
//    companion object {
//        private const val SEARCH_EDIT_TEXT_TRACK_DELAY = 2000L
//        private const val SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY = 0L
//        private const val TRACK_ITEM_CLICKED_DELAY = 500L
//        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
//    }

}