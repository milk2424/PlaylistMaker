package com.example.playlistmaker.domain.search.interactor

import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song
import kotlinx.coroutines.flow.Flow

interface SongsInteractor {
    fun loadSongsFromApi(songName: String): Flow<ResponseStatus>

    fun loadSongHistory(): List<Song>

    fun addSongToHistory(song: Song)

    fun clearSongHistory()

}