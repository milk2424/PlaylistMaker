package com.example.playlistmaker.domain.search.model

import java.io.Serializable

data class Song(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Serializable