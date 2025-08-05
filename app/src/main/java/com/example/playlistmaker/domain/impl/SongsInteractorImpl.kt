package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.entity.Song
import com.example.playlistmaker.domain.interactor.SongsInteractor
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import java.util.concurrent.Executors

class SongsInteractorImpl(
    private val songsRepository: SongsRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : SongsInteractor {

    private val executor = Executors.newSingleThreadExecutor()

    override fun loadSongsFromApi(songName: String, consumer: SongsInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(songsRepository.searchSongs(songName))
        }
    }

    override fun loadSongHistory(): List<Song> {
        return (searchHistoryRepository.loadSongHistory())
    }

    override fun addSongToHistory(song: Song) {
        executor.execute {
            searchHistoryRepository.addSongToHistory(song)
        }
    }

    override fun clearSongHistory() {
        executor.execute {
            searchHistoryRepository.clearSongHistory()
        }
    }
}