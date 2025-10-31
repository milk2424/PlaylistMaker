package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.db.PlaylistMakerDB
import com.example.playlistmaker.data.favourite_songs.utils.SongEntityMapper
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.ITunesService
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
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

    single {
        Room
            .databaseBuilder(androidContext(), PlaylistMakerDB::class.java, "database.db")
            .build()
    }

    factory {
        SongEntityMapper()
    }
}