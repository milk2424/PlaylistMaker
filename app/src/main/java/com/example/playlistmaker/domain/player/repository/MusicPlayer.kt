package com.example.playlistmaker.domain.player.repository

import com.example.playlistmaker.presentation.utils.player.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface MusicPlayer {

    fun start()

    fun pause()

    fun getPlayerState(): StateFlow<PlayerState>

    fun showNotification()

    fun removeNotification()

    fun stop()
}