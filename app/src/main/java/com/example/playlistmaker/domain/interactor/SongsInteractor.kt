package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.entity.ResponseStatus
import com.example.playlistmaker.domain.entity.Song

interface SongsInteractor {
    fun loadSongsFromApi(songName: String, consumer: TracksConsumer)

    fun loadSongHistory():List<Song>

    fun addSongToHistory(song: Song)

    fun clearSongHistory()

    interface TracksConsumer {
        fun consume(response: ResponseStatus<List<Song>>)
    }
}