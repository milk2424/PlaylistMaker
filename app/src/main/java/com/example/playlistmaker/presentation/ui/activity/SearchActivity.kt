package com.example.playlistmaker.presentation.ui.activity

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
import com.example.playlistmaker.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.entity.ResponseStatus
import com.example.playlistmaker.domain.entity.Song
import com.example.playlistmaker.domain.interactor.SongsInteractor
import com.example.playlistmaker.presentation.ui.recycler_vIew.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var savedEditTextValue: String? = ""

    private val trackList = mutableListOf<Song>()
    private val trackAdapter = TrackAdapter(trackList)

    private var isTrackItemCanBeClicked = true
    private val getTrackListRunnable = Runnable { getTrackList() }
    private val mainHandler by lazy { Handler(mainLooper) }

    private lateinit var songsInteractor: SongsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsInteractor = Creator.provideSongsInteractor()

        binding.trackRcView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.trackRcView.adapter = trackAdapter

        binding.btnBackSearch.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.searchEditText.setText(savedEditTextValue ?: "")

        binding.btnClearEditText.setOnClickListener {
            binding.searchEditText.setText("")
            clearEditTextFocus()
            binding.errorLoadingTracksLayout.visibility = GONE
            binding.errorBadConnectionLayout.visibility = GONE
            mainHandler.removeCallbacks(getTrackListRunnable)
            trackList.clear()
            trackList.addAll(loadSongHistory())
            trackAdapter.notifyDataSetChanged()
        }

        fun isHistoryVisible(state: Boolean) {
            binding.errorLoadingTracksLayout.visibility = GONE
            binding.errorBadConnectionLayout.visibility = GONE
            binding.trackRcView.visibility = if (state) VISIBLE else GONE
            binding.clearSearchHistoryBtn.visibility = if (state) VISIBLE else GONE
            binding.yourSearch.visibility = if (state) VISIBLE else GONE
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            songsInteractor.clearSongHistory()
            isHistoryVisible(false)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                savedEditTextValue = input
                if (input.isEmpty()) {
                    binding.btnClearEditText.visibility = GONE
                    mainHandler.removeCallbacks(getTrackListRunnable)
                    trackList.clear()
                    trackList.addAll(loadSongHistory())
                    trackAdapter.notifyDataSetChanged()
                    isHistoryVisible(trackList.isNotEmpty())
                } else {
                    binding.btnClearEditText.visibility = VISIBLE
                    isHistoryVisible(false)
                    debounceSearchTrack(SEARCH_EDIT_TEXT_TRACK_DELAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearEditTextFocus()
                if (binding.searchEditText.text.isNotEmpty()) debounceSearchTrack(
                    SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY
                )
                true
            } else false
        }

        binding.retrySearch.setOnClickListener {
            debounceSearchTrack(SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY)
        }

        trackAdapter.onClickCallback = { song ->
            if (debounceTrackItemClicked()) {
                songsInteractor.addSongToHistory(song)
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PLAY_TRACK, song)
                startActivity(intent)
            }
        }

        val initSongHistory = loadSongHistory()

        if (initSongHistory.isNotEmpty()) {
            trackList.addAll(initSongHistory)
            trackAdapter.notifyDataSetChanged()
            isHistoryVisible(true)
        } else isHistoryVisible(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE_KEY, savedEditTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE_KEY)
    }

    private fun getTrackList() {
        binding.clearSearchHistoryBtn.visibility = GONE
        binding.btnClearEditText.visibility = GONE
        binding.progressBarSearch.visibility = VISIBLE
        trackList.clear()
        val handler = Handler(mainLooper)
        songsInteractor.loadSongsFromApi(
            binding.searchEditText.text.toString(),
            object : SongsInteractor.TracksConsumer {
                override fun consume(response: ResponseStatus<List<Song>>) {
                    handler.post {
                        when (response) {
                            is ResponseStatus.Successful -> {
                                if (response.songs.isEmpty()) {
                                    setUIAfterSongResponse(isError = false, isEmpty = true)
                                } else {
                                    setUIAfterSongResponse(isError = false, isEmpty = false)
                                    trackList.addAll(response.songs)
                                }
                            }

                            is ResponseStatus.Error -> {
                                setUIAfterSongResponse(isError = true, isEmpty = false)
                            }
                        }
                        trackAdapter.notifyDataSetChanged()
                        binding.progressBarSearch.visibility = GONE
                    }
                }
            })
    }

    private fun debounceSearchTrack(delay: Long) {
        mainHandler.removeCallbacks(getTrackListRunnable)
        mainHandler.postDelayed(getTrackListRunnable, delay)
    }

    private fun debounceTrackItemClicked(): Boolean {
        val currentIsTrackItemCanBeClicked = isTrackItemCanBeClicked
        if (currentIsTrackItemCanBeClicked) {
            isTrackItemCanBeClicked = false
            mainHandler.postDelayed({ isTrackItemCanBeClicked = true }, TRACK_ITEM_CLICKED_DELAY)
        }
        return currentIsTrackItemCanBeClicked
    }

    private fun loadSongHistory() = songsInteractor.loadSongHistory().reversed()

    private fun setUIAfterSongResponse(isError: Boolean, isEmpty: Boolean) {
        if (isError) {
            binding.trackRcView.visibility = GONE
            binding.errorBadConnectionLayout.visibility = VISIBLE
            binding.errorLoadingTracksLayout.visibility = GONE
            clearEditTextFocus()
        } else {
            binding.trackRcView.visibility = if (isEmpty) GONE else VISIBLE
            binding.errorBadConnectionLayout.visibility = GONE
            binding.errorLoadingTracksLayout.visibility = if (isEmpty) VISIBLE else GONE
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
        mainHandler.removeCallbacks(getTrackListRunnable)
    }

    companion object {
        private const val SEARCH_EDIT_TEXT_TRACK_DELAY = 2000L
        private const val SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY = 0L
        private const val TRACK_ITEM_CLICKED_DELAY = 500L
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
        const val PLAY_TRACK = "PLAY_TRACK"
    }

}