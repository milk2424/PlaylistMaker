package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.SongsRequest
import com.example.playlistmaker.data.search.dto.SongsResponse
import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.domain.search.repository.SongsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {
    override fun searchSongs(songName: String): Flow<ResponseStatus> = flow {
        val response = networkClient.sendRequest(SongsRequest(songName))
        if (response.responseCode == 200 && (response as SongsResponse).resultCount == 0) emit(
            ResponseStatus.Empty
        )
        else if (response.responseCode == 200) {
            emit(ResponseStatus.Successful((response as SongsResponse).results.map {
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
            }))

        } else emit(ResponseStatus.Error(response.responseCode))
    }
}