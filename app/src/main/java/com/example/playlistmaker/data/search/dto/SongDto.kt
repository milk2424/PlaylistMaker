package com.example.playlistmaker.data.search.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongDto(
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

}
