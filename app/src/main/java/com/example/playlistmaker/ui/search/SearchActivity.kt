package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.search.SongState
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.player.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    private val viewModel: SearchViewModel by viewModel()

    private var savedEditTextValue: String? = ""

    private val songAdapter = SongAdapter()

    private var isSongItemCanBeClicked = true
    private val getSongListRunnable =
        Runnable { viewModel.loadSongsFromApi(binding.searchEditText.text.toString()) }
    private val mainHandler by lazy { Handler(mainLooper) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.trackRcView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.trackRcView.adapter = songAdapter

        binding.btnBackSearch.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnClearEditText.setOnClickListener {
            binding.searchEditText.setText("")
            clearEditTextFocus()
            viewModel.loadHistory()
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
            isHistoryVisible(false)
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
                        mainHandler.removeCallbacks(getSongListRunnable)
                        viewModel.loadHistory()
                        binding.btnClearEditText.visibility = GONE
                    } else {
                        binding.btnClearEditText.visibility = VISIBLE
                        debounceSearchTrack(SEARCH_EDIT_TEXT_TRACK_DELAY)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearEditTextFocus()
                    if (binding.searchEditText.text.isNotEmpty()) debounceSearchTrack(
                        SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY
                    )
                    true
                } else false
            }
        }

        binding.retrySearch.setOnClickListener {
            debounceSearchTrack(SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY)
        }

        songAdapter.onClickCallback = { song ->
            if (debounceTrackItemClicked()) {
                viewModel.addSongToHistory(song)
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PLAY_TRACK, song)
                startActivity(intent)
            }
        }

        viewModel.songStateLiveData().observe(this) { songState ->
            setUIAfterSongResponse(songState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE_KEY, savedEditTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE_KEY)
    }

    private fun debounceSearchTrack(delay: Long) {
        mainHandler.removeCallbacks(getSongListRunnable)
        mainHandler.postDelayed(getSongListRunnable, delay)
    }

    private fun debounceTrackItemClicked(): Boolean {
        val currentIsTrackItemCanBeClicked = isSongItemCanBeClicked
        if (currentIsTrackItemCanBeClicked) {
            isSongItemCanBeClicked = false
            mainHandler.postDelayed({ isSongItemCanBeClicked = true }, TRACK_ITEM_CLICKED_DELAY)
        }
        return currentIsTrackItemCanBeClicked
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
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(getSongListRunnable)
    }

    companion object {
        private const val SEARCH_EDIT_TEXT_TRACK_DELAY = 2000L
        private const val SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY = 0L
        private const val TRACK_ITEM_CLICKED_DELAY = 500L
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
        const val PLAY_TRACK = "PLAY_TRACK"
    }

}