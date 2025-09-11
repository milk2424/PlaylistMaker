package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    SearchHistoryRepository {
    private var _listOfHistory = mutableListOf<Song>()

    private val listOfHistory: List<Song> get() = _listOfHistory.toList()

    override fun loadSongHistory(): List<Song> {
        val json = sharedPreferences.getString(TRACK_HISTORY, "")
        _listOfHistory = if (!json.isNullOrEmpty()) gson.fromJson(json, Array<Song>::class.java)
            .toMutableList()
        else mutableListOf()
        return listOfHistory
    }

    override fun addSongToHistory(song: Song) {
        _listOfHistory.apply {
            remove(song)
            add(song)
            if (size > TRACK_HISTORY_SIZE) repeat(size - TRACK_HISTORY_SIZE) {
                removeAt(0)
            }
        }
        sharedPreferences.edit().putString(TRACK_HISTORY, gson.toJson(listOfHistory)).apply()
    }

    override fun clearSongHistory() {
        sharedPreferences.edit().clear().apply()
        _listOfHistory.clear()
    }

    init {
        loadSongHistory()
    }

    companion object {
        private const val TRACK_HISTORY_SIZE = 10
        const val TRACK_HISTORY = "TRACK_HISTORY"
    }
}