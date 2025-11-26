package com.example.playlistmaker.di

import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.search.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.search.interactor.SongsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.sharing.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.repository.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {
    single<SongsInteractor> {
        SongsInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidContext().applicationContext)
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get(), get())
    }
}