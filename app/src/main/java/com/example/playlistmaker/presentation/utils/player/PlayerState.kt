package com.example.playlistmaker.presentation.utils.player


sealed class PlayerState(open val time: String) {
    data class Default(override val time: String = "00:00") : PlayerState(time)
    data class Prepared(override val time: String = "00:00") : PlayerState(time)
    data class Playing(override val time: String) : PlayerState(time)
    data class Paused(override val time: String) : PlayerState(time)
}