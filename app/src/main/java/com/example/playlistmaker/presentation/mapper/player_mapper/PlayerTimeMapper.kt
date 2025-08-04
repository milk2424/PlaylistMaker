package com.example.playlistmaker.presentation.mapper.player_mapper

import java.text.SimpleDateFormat
import java.util.Locale

object PlayerTimeMapper {
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun map(time: Int): String {
        return dateFormat.format(time)
    }

    fun map(time: Long): String {
        return dateFormat.format(time)
    }
}