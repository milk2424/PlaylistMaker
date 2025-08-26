package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.ITunesService
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesService> {
        val baseUrl = "https://itunes.apple.com"
        val retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create(ITunesService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
}