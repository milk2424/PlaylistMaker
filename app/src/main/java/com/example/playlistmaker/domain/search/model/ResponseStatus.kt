package com.example.playlistmaker.domain.search.model

sealed interface ResponseStatus {
    class Successful(val songs: List<Song>) : ResponseStatus
    data object Empty : ResponseStatus
    class Error(val responseCode: Int) : ResponseStatus
}