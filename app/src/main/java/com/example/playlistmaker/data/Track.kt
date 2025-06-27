package com.example.playlistmaker.data

data class Track(
    val id: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String,
    val country: String
) {

    fun getPlayerImageURL(): String = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
