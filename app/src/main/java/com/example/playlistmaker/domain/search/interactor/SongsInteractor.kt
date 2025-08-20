package com.example.playlistmaker.domain.search.interactor

import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song

interface SongsInteractor {
    fun loadSongsFromApi(songName: String, consumer: TracksConsumer)

    fun loadSongHistory():List<Song>

    fun addSongToHistory(song: Song)

    fun clearSongHistory()

    interface TracksConsumer {
        fun consume(response: ResponseStatus)
    }
}