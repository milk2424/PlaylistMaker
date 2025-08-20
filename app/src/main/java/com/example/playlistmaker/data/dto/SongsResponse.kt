package com.example.playlistmaker.data.dto

data class SongsResponse(
    val resultCount: Int,
    val results: List<SongDto>
) : Response()