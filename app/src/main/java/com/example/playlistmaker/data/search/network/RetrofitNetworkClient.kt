package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SongsRequest

class RetrofitNetworkClient(private val iTunesService: ITunesService) : NetworkClient {
    override suspend fun sendRequest(dto: Any): Response {
        when (dto) {
            is SongsRequest -> {
                try {
                    val response = iTunesService.searchSongs(dto.songName)

                    return response.apply { responseCode = 200 }

                } catch (e: Throwable) {
                    return Response().apply { responseCode = 500 }
                }
            }
            else -> return Response().apply { responseCode = 400 }
        }
    }
}