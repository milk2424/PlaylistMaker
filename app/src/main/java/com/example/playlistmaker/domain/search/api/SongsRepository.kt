package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.ResponseStatus

interface SongsRepository {
    fun searchSongs(songName:String): ResponseStatus
}