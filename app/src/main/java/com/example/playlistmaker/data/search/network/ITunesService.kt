package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.SongsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

    @GET("/search?entity=song")
    fun searchSongs(@Query("term") text:String) : Call<SongsResponse>
}