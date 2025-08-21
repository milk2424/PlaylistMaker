package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.Song

interface SearchHistoryRepository {
    fun loadSongHistory(): List<Song>

    fun addSongToHistory(song: Song)

    fun clearSongHistory()
}