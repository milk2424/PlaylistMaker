package com.example.playlistmaker.data.search.dto

data class SongsResponse(
    val resultCount: Int,
    val results: List<SongDto>
) : Response()