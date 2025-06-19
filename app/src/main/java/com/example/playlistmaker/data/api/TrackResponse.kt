package com.example.playlistmaker.data.api

import com.example.playlistmaker.data.Track

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)