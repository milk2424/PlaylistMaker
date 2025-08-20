package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entity.Song

interface SearchHistoryRepository {
    fun loadSongHistory(): List<Song>

    fun addSongToHistory(song: Song)

    fun clearSongHistory()
}