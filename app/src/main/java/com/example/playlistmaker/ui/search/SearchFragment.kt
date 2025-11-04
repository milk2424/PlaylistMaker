package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.search.SongState
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.song_rc_view.SongAdapter
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : FragmentBinding<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModel()

    private var savedEditTextValue: String? = ""

    private var songAdapter = SongAdapter()

    private lateinit var onItemClick: (Song) -> Unit

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trackRcView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.trackRcView.adapter = songAdapter

        binding.btnClearEditText.setOnClickListener {
            binding.searchEditText.setText("")
            clearEditTextFocus()
            viewModel.loadHistory()
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
            isHistoryVisible(false)
        }

        onItemClick = debounce(
            TRACK_ITEM_CLICKED_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { song ->
            viewModel.addSongToHistory(song)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToPlayerFragment(
                    song
                )
            )
        }

        binding.searchEditText.apply {
            setText(savedEditTextValue ?: "")
            setOnFocusChangeListener { v, hasFocus ->
                binding.btnClearEditText.visibility =
                    if (hasFocus && !savedEditTextValue.isNullOrEmpty()) VISIBLE else GONE
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s.toString()
                    savedEditTextValue = input
                    if (input.isEmpty()) {
                        viewModel.loadHistory()
                        binding.btnClearEditText.visibility = GONE
                    } else {
                        binding.btnClearEditText.visibility = VISIBLE
                        viewModel.loadSongsFromApi(
                            getSongCurrentInput(),
                            SEARCH_EDIT_TEXT_TRACK_DELAY
                        )
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearEditTextFocus()
                    if (binding.searchEditText.text.isNotEmpty()) viewModel.loadSongsFromApi(
                        getSongCurrentInput(),
                        SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY
                    )
                    true
                } else false
            }
        }

        binding.retrySearch.setOnClickListener {
            viewModel.loadSongsFromApi(
                getSongCurrentInput(),
                SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY
            )
        }

        songAdapter.onClickCallback = { song ->
            onItemClick(song)
        }

        viewModel.songStateLiveData().observe(viewLifecycleOwner) { songState ->
            setUIAfterSongResponse(songState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE_KEY, savedEditTextValue)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedEditTextValue = savedInstanceState?.getString(EDIT_TEXT_VALUE_KEY) ?: ""
    }

    private fun isHistoryVisible(state: Boolean, songs: List<Song> = listOf()) {
        binding.clearSearchHistoryBtn.visibility =
            if (state && songs.isNotEmpty()) VISIBLE else GONE
        binding.yourSearch.visibility = if (state && songs.isNotEmpty()) VISIBLE else GONE
        songAdapter.resetAdapter(songs)
    }

    private fun setUIAfterSongResponse(state: SongState) {
        when (state) {
            is SongState.Empty -> {
                binding.progressBarSearch.visibility = GONE
                binding.trackRcView.visibility = GONE
                isHistoryVisible(false)
                binding.errorBadConnectionLayout.visibility = GONE
                binding.errorLoadingTracksLayout.visibility = VISIBLE
            }

            is SongState.History -> {
                binding.progressBarSearch.visibility = GONE
                isHistoryVisible(true, state.songs)
                binding.trackRcView.visibility = if (state.songs.isNotEmpty()) VISIBLE else GONE
                binding.errorLoadingTracksLayout.visibility = GONE
                binding.errorBadConnectionLayout.visibility = GONE
            }

            is SongState.Loading -> {
                binding.progressBarSearch.visibility = VISIBLE
                isHistoryVisible(false)
                binding.trackRcView.visibility = GONE
                binding.errorLoadingTracksLayout.visibility = GONE
                binding.errorBadConnectionLayout.visibility = GONE
            }

            is SongState.NetworkError -> {
                binding.progressBarSearch.visibility = GONE
                isHistoryVisible(false)
                binding.trackRcView.visibility = GONE
                binding.errorLoadingTracksLayout.visibility = GONE
                binding.errorBadConnectionLayout.visibility = VISIBLE
            }

            is SongState.Successful -> {
                binding.progressBarSearch.visibility = GONE
                isHistoryVisible(false)
                if (binding.searchEditText.text.isEmpty()) viewModel.loadHistory() else songAdapter.resetAdapter(
                    state.songs
                )
                binding.trackRcView.visibility = VISIBLE
                binding.errorLoadingTracksLayout.visibility = GONE
                binding.errorBadConnectionLayout.visibility = GONE
            }
        }
    }

    private fun clearEditTextFocus() {
        binding.searchEditText.clearFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun getSongCurrentInput() = binding.searchEditText.text.toString()

    companion object {
        private const val SEARCH_EDIT_TEXT_TRACK_DELAY = 2000L
        private const val SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY = 0L
        private const val TRACK_ITEM_CLICKED_DELAY = 500L
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
    }

}