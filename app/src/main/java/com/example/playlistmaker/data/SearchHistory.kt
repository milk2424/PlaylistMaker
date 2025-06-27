package com.example.playlistmaker.data

import android.content.SharedPreferences
import android.system.Os.remove
import android.util.Log
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private var _listOfHistory = mutableListOf<Track>()

    val listOfHistory: List<Track> get() = _listOfHistory.toList()

    private fun loadHistory() {
        val json = sharedPreferences.getString(TRACK_HISTORY, "")
        if (!json.isNullOrEmpty())
            _listOfHistory = Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    fun addTrackToHistory(track: Track) {
        _listOfHistory.apply {
            remove(track)
            add(track)
            if (size > TRACK_HISTORY_SIZE) repeat(size - TRACK_HISTORY_SIZE) {
                removeAt(0)
            }
        }
        Log.d("TESTGSON", "addTrackToHistory: ${listOfHistory.toString()}")
        sharedPreferences.edit().putString(TRACK_HISTORY, Gson().toJson(listOfHistory)).apply()
    }

    fun clearTrackHistory() {
        sharedPreferences.edit().clear().apply()
        _listOfHistory.clear()
    }

    init {
        loadHistory()
    }

    companion object {
        private const val TRACK_HISTORY_SIZE = 10
        private const val TRACK_HISTORY = "TRACK_HISTORY"
    }
}