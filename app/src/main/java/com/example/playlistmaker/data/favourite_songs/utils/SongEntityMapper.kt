package com.example.playlistmaker.data.favourite_songs.utils

import com.example.playlistmaker.data.db.entity.SongEntity
import com.example.playlistmaker.domain.search.model.Song

class SongEntityMapper {
    fun map(song: Song) = SongEntity(
        song.trackId,
        song.trackName,
        song.artistName,
        song.trackTimeMillis,
        song.artworkUrl100,
        song.collectionName,
        song.releaseDate,
        song.primaryGenreName,
        song.country,
        song.previewUrl
    )

    fun map(song: SongEntity) = Song(
        song.id,
        song.trackName,
        song.artistName,
        song.trackTimeMillis,
        song.artworkUrl100,
        song.collectionName,
        song.releaseDate,
        song.primaryGenreName,
        song.country,
        song.previewUrl
    )
}