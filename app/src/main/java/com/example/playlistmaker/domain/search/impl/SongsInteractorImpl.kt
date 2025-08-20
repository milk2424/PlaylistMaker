package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SongsRepository
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.domain.search.interactor.SongsInteractor
import com.example.playlistmaker.domain.search.repository.SearchHistoryRepository
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