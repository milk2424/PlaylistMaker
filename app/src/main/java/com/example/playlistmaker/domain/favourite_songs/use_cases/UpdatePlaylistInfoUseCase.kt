package com.example.playlistmaker.domain.favourite_songs.use_cases

import android.net.Uri
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository

class UpdatePlaylistInfoUseCase(private val repository: PlaylistsRepository) {

    operator fun invoke(playlistId: Int, name: String, description: String, uri: Uri?) {
        repository.updatePlaylistInfo(playlistId, name, description, uri)
    }
}