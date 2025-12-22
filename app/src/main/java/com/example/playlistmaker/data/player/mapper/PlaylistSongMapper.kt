package com.example.playlistmaker.data.player.mapper

import com.example.playlistmaker.data.db.entity.PlaylistSongEntity
import com.example.playlistmaker.domain.search.model.Song

class PlaylistSongMapper {
    fun map(song: Song, playlistId: Int) = PlaylistSongEntity(
        id = 0,
        songId = song.trackId,
        playlistId = playlistId,
        trackName = song.trackName,
        artistName = song.artistName,
        trackTimeMillis = song.trackTimeMillis,
        artworkUrl100 = song.artworkUrl100,
        collectionName = song.collectionName,
        releaseDate = song.collectionName,
        primaryGenreName = song.primaryGenreName,
        country = song.country,
        previewUrl = song.previewUrl
    )

    fun map(song: PlaylistSongEntity) = Song(
        trackId = song.songId,
        trackName = song.trackName,
        artistName = song.artistName,
        trackTimeMillis = song.trackTimeMillis,
        artworkUrl100 = song.artworkUrl100,
        collectionName = song.collectionName,
        releaseDate = song.collectionName,
        primaryGenreName = song.primaryGenreName,
        country = song.country,
        previewUrl = song.previewUrl
    )
}