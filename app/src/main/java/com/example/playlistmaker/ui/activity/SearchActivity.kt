package com.example.playlistmaker.ui.activity

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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.Companion.SHARED_PREFS
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.data.api.ITunesService
import com.example.playlistmaker.data.api.TrackResponse
import com.example.playlistmaker.ui.recyclerVIew.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val itunesService = retrofit.create(ITunesService::class.java)

    private var savedEditTextValue: String? = ""
    private lateinit var rcView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var noTrackLayout: LinearLayout
    private lateinit var badConnectionErrorLayout: LinearLayout
    private lateinit var progressBarSearch: ProgressBar
    private val trackList = mutableListOf<Track>()
    private val trackAdapter = TrackAdapter(trackList)

    private var isTrackItemClicked = false
    private var getTrackListRunnable = Runnable {}
    private val mainHandler by lazy { Handler(mainLooper) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchHistory = SearchHistory(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))
        noTrackLayout = findViewById(R.id.error_loading_tracks_layout)

        badConnectionErrorLayout = findViewById(R.id.error_bad_connection_layout)

        rcView = findViewById(R.id.track_rcView)
        rcView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcView.adapter = trackAdapter

        val btnBack = findViewById<TextView>(R.id.btn_back_search)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        progressBarSearch = findViewById(R.id.progress_bar_search)

        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.setText(savedEditTextValue ?: "")

        val clearTextBtn = findViewById<TextView>(R.id.btn_clear_edit_text)

        clearTextBtn.setOnClickListener {
            searchEditText.setText("")
            clearEditTextFocus()
            noTrackLayout.visibility = GONE
            badConnectionErrorLayout.visibility = GONE
            mainHandler.removeCallbacks(getTrackListRunnable)
            trackList.clear()
            trackList.addAll(searchHistory.listOfHistory.reversed())
            trackAdapter.notifyDataSetChanged()
        }

        val clearHistoryBtn = findViewById<Button>(R.id.clear_search_history_btn)

        val historyText = findViewById<TextView>(R.id.your_search)

        fun isHistoryVisible(state: Boolean) {
            noTrackLayout.visibility = GONE
            badConnectionErrorLayout.visibility = GONE
            rcView.visibility = if (state) VISIBLE else GONE
            clearHistoryBtn.visibility = if (state) VISIBLE else GONE
            historyText.visibility = if (state) VISIBLE else GONE
        }


        clearHistoryBtn.setOnClickListener {
            searchHistory.clearTrackHistory()
            isHistoryVisible(false)
        }

        getTrackListRunnable = Runnable {
            getTrackList()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                savedEditTextValue = input
                if (input.isEmpty()) {
                    clearTextBtn.visibility = GONE
                    mainHandler.removeCallbacks(getTrackListRunnable)
                    trackList.clear()
                    trackList.addAll(searchHistory.listOfHistory.reversed())
                    trackAdapter.notifyDataSetChanged()
                    isHistoryVisible(trackList.isNotEmpty())
                } else {
                    clearTextBtn.visibility = VISIBLE
                    isHistoryVisible(false)
                    debounceSearchTrack(SEARCH_EDIT_TEXT_TRACK_DELAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearEditTextFocus()
                if (searchEditText.text.isNotEmpty())
                    debounceSearchTrack(SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY)
                true
            } else false
        }

        val retrySearchBtn = findViewById<Button>(R.id.retry_search)
        retrySearchBtn.setOnClickListener {
            debounceSearchTrack(SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY)
        }

        trackAdapter.onClickCallback = { track ->
            if (!debounceTrackItemClicked()) {
                searchHistory.addTrackToHistory(track)
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PLAY_TRACK, track)
                startActivity(intent)
            }
        }


        if (searchHistory.listOfHistory.isNotEmpty()) {
            trackList.addAll(searchHistory.listOfHistory.reversed())
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


    fun clearEditTextFocus() {
        searchEditText.clearFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    private fun getTrackList() {
        val clearHistoryBtn = findViewById<Button>(R.id.clear_search_history_btn)
        val historyText = findViewById<TextView>(R.id.your_search)
        clearHistoryBtn.visibility = GONE
        historyText.visibility = GONE
        progressBarSearch.visibility = VISIBLE
        itunesService.search(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    trackList.clear()
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isEmpty() != false) {
                                rcView.visibility = GONE
                                badConnectionErrorLayout.visibility = GONE
                                noTrackLayout.visibility = VISIBLE
                            } else {
                                rcView.visibility = VISIBLE
                                badConnectionErrorLayout.visibility = GONE
                                noTrackLayout.visibility = GONE
                                trackList.addAll(response.body()?.results!!)
                            }
                            trackAdapter.notifyDataSetChanged()
                        }

                        else -> {
                            rcView.visibility = GONE
                            badConnectionErrorLayout.visibility = VISIBLE
                            noTrackLayout.visibility = GONE
                            clearEditTextFocus()
                        }
                    }
                    progressBarSearch.visibility = GONE
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    trackList.clear()
                    rcView.visibility = GONE
                    badConnectionErrorLayout.visibility = VISIBLE
                    noTrackLayout.visibility = GONE
                    progressBarSearch.visibility = GONE
                    clearEditTextFocus()
                }

            })
    }

    private fun debounceSearchTrack(delay: Long) {
        mainHandler.removeCallbacks(getTrackListRunnable)
        mainHandler.postDelayed(getTrackListRunnable, delay)
    }

    private fun debounceTrackItemClicked(): Boolean {
        val currentIsTrackItemClicked = isTrackItemClicked
        if (!currentIsTrackItemClicked) {
            isTrackItemClicked = true
            mainHandler.postDelayed({ isTrackItemClicked = false }, TRACK_ITEM_CLICKED_DELAY)
        }
        return currentIsTrackItemClicked
    }


    companion object {
        private const val SEARCH_EDIT_TEXT_TRACK_DELAY = 2000L
        private const val SEARCH_BUTTON_ENTER_PRESSED_TRACK_DELAY = 0L
        private const val TRACK_ITEM_CLICKED_DELAY = 500L
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
        const val PLAY_TRACK = "PLAY_TRACK"
    }

}