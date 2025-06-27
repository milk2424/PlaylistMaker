package com.example.playlistmaker.data

data class Track(
    val id:String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
