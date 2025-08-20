package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.ResponseStatus
import com.example.playlistmaker.domain.entity.Song

interface SongsRepository {
    fun searchSongs(songName:String): ResponseStatus<List<Song>>
}