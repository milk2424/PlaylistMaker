package com.example.playlistmaker.creator

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.SHARED_PREFS
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SongsRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesService
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.search.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.search.interactor.SongsInteractor
import com.example.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.search.repository.SongsRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.sharing.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.repository.SharingRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository(), getSearchHistoryRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getThemeRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(), getSharingRepository())
    }

    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()

    private fun getThemeRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPreferences())
    }

    private fun getSharingRepository(): SharingRepository {
        return SharingRepositoryImpl(App.instance.applicationContext)
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

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(App.instance.applicationContext)
    }

}