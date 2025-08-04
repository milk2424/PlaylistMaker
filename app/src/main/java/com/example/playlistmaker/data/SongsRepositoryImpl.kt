package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SongsRequest
import com.example.playlistmaker.data.dto.SongsResponse
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.entity.ResponseStatus
import com.example.playlistmaker.domain.entity.Song

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {
    override fun searchSongs(songName: String): ResponseStatus<List<Song>> {
        val response = networkClient.sendRequest(SongsRequest(songName))
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

        } else ResponseStatus.Error<Nothing>(response.responseCode)
    }
}