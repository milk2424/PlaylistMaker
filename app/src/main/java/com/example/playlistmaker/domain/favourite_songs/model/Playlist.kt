package com.example.playlistmaker.domain.favourite_songs.model

import java.io.Serializable

data class Playlist(
    val id: Int?,
    val name: String,
    val description: String,
    val songs: String,
    val songsCount: Int,
    val image: String?
) : Serializable