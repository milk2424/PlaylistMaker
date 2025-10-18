package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.ResponseStatus
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
    fun searchSongs(songName:String): Flow<ResponseStatus>
}