package com.example.playlistmaker.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
    private val trackList = mutableListOf<Track>()
    private val trackAdapter = TrackAdapter(trackList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchHistory = SearchHistory(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))
        noTrackLayout = findViewById<LinearLayout>(R.id.error_loading_tracks_layout)

        badConnectionErrorLayout = findViewById<LinearLayout>(R.id.error_bad_connection_layout)

        rcView = findViewById(R.id.track_rcView)
        rcView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcView.adapter = trackAdapter
        val btnBack = findViewById<TextView>(R.id.btn_back_search)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.setText(savedEditTextValue ?: "")
        val clearTextBtn = findViewById<TextView>(R.id.btn_clear_edit_text)

        clearTextBtn.setOnClickListener {
            searchEditText.setText("")
            clearEditTextFocus()
            noTrackLayout.visibility = GONE
            badConnectionErrorLayout.visibility = GONE
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
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                savedEditTextValue = input
                if (input.isEmpty()) {
                    clearTextBtn.visibility = GONE
                    trackList.clear()
                    trackList.addAll(searchHistory.listOfHistory.reversed())
                    trackAdapter.notifyDataSetChanged()
                    isHistoryVisible(trackList.isNotEmpty())
                } else {
                    clearTextBtn.visibility = VISIBLE
                    isHistoryVisible(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearEditTextFocus()
                if (searchEditText.text.isNotEmpty())
                    getTrackList()
                true
            } else false
        }

        val retrySearchBtn = findViewById<Button>(R.id.retry_search)
        retrySearchBtn.setOnClickListener {
            getTrackList()
        }

        trackAdapter.onClickCallback = { track ->
            searchHistory.addTrackToHistory(track)
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
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    trackList.clear()
                    rcView.visibility = GONE
                    badConnectionErrorLayout.visibility = VISIBLE
                    noTrackLayout.visibility = GONE
                    clearEditTextFocus()
                }

            })
    }


    companion object {
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
    }

}