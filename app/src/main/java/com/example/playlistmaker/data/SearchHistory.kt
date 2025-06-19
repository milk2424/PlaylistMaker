package com.example.playlistmaker.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    var listOfHistory = mutableListOf<Track>()

    private fun loadHistory() {
        val json = sharedPreferences.getString(TRACK_HISTORY, "")
        if (!json.isNullOrEmpty())
            listOfHistory = Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    fun addTrackToHistory(track: Track) {
        listOfHistory.apply {
            remove(track)
            add(track)
            if (size > 10) repeat(size - 10) {
                removeAt(0)
            }
        }
        Log.d("TESTGSON", "addTrackToHistory: ${listOfHistory.toString()}")
        sharedPreferences.edit().putString(TRACK_HISTORY, Gson().toJson(listOfHistory)).apply()
    }

    fun clearTrackHistory() {
        sharedPreferences.edit().clear().apply()
        listOfHistory.clear()
    }

    init {
        loadHistory()
    }

    companion object {
        private const val TRACK_HISTORY = "TRACK_HISTORY"
    }
}