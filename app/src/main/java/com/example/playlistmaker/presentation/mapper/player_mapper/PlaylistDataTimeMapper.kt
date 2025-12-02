package com.example.playlistmaker.presentation.mapper.player_mapper

import kotlin.time.Duration.Companion.milliseconds

object PlaylistDataTimeMapper {
    fun map(time: Long): Int {
        return time.milliseconds.inWholeMinutes.toInt()
    }
}