package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SongsRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesService::class.java)

    override fun sendRequest(dto: Any): Response {
        when (dto) {
            is SongsRequest -> {
                try {
                    val response = itunesService.searchSongs(dto.songName).execute()

                    val body = response.body() ?: Response()

                    return body.apply { responseCode = response.code() }
                } catch (e: Exception) {
                    return Response().apply { responseCode = 500 }
                }
            }

            else -> return Response().apply { responseCode = 400 }
        }
    }

}