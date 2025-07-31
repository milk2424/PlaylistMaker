package com.example.playlistmaker.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: String? = null,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable {

    fun getPlayerImageURL(): String = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
