package com.example.playlistmaker.presentation.mapper.player_mapper

object PlayerImageMapper {
    fun map(oldUrl: String): String {
        return oldUrl.replaceAfterLast('/', "512x512bb.jpg")
    }
}