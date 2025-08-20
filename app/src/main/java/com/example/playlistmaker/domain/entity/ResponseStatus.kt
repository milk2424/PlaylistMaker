package com.example.playlistmaker.domain.entity

sealed class ResponseStatus<out T> {
    class Successful<out T>(val songs: List<Song>) : ResponseStatus<T>()
    class Error<out T>(val responseCode: Int) : ResponseStatus<T>()
}