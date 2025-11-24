package com.example.playlistmaker.data.favourite_songs.utils

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistMapper(private val gson: Gson) {
    fun map(playlist: PlaylistEntity) = Playlist(
        playlist.id,
        playlist.name,
        playlist.description,
        playlist.songs,
        songsCount(playlist.songs),
        playlist.image
    )

    fun map(playlist: Playlist) = PlaylistEntity(
        playlist.id ?: 0,
        playlist.name,
        playlist.description,
        playlist.songs,
        playlist.image
    )

    private fun songsCount(songs: String): Int {
        if (songs.isBlank()) return 0
        val typeToken = object : TypeToken<List<Int>>() {}.type
        val list: List<Int> = gson.fromJson(songs, typeToken)
        return list.size
    }

    fun toSongsIdsList(string: String): List<String> {
        val typeToken = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, typeToken)
    }

    fun toSongsIdsString(list: List<String>): String {
        return gson.toJson(list)
    }
}