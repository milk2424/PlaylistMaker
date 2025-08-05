package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.App.Companion.SHARED_PREFS
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.SongsRepositoryImpl
import com.example.playlistmaker.data.ThemeRepositoryImpl
import com.example.playlistmaker.data.network.ITunesService
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.interactor.SongsInteractor
import com.example.playlistmaker.domain.interactor.ThemeInteractor
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.ThemeRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository(), getSearchHistoryRepository())
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository())
    }

    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(getSharedPreferences())
    }

    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(getNetworkClient())
    }

    private fun getNetworkClient(): NetworkClient {
        return RetrofitNetworkClient(getITunesService())
    }

    private fun getITunesService(): ITunesService {
        val baseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ITunesService::class.java)
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getSharedPreferences())
    }

    private fun getSharedPreferences(): SharedPreferences {
        return App.instance.applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    }

}