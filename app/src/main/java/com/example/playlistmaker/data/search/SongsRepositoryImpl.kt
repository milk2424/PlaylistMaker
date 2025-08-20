package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.SongsRequest
import com.example.playlistmaker.data.search.dto.SongsResponse
import com.example.playlistmaker.domain.search.api.SongsRepository
import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {
    override fun searchSongs(songName: String): ResponseStatus {
        val response = networkClient.sendRequest(SongsRequest(songName))
        if (response.responseCode == 200 && (response as SongsResponse).resultCount == 0) return ResponseStatus.Empty
        return if (response.responseCode == 200) {
            ResponseStatus.Successful((response as SongsResponse).results.map {
                Song(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            })

        } else ResponseStatus.Error(response.responseCode)
    }
}