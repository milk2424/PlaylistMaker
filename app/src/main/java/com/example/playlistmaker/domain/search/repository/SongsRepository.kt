package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.ResponseStatus

interface SongsRepository {
    fun searchSongs(songName:String): ResponseStatus
}