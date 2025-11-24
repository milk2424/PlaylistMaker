package com.example.playlistmaker.domain.favourite_songs.use_cases

import android.net.Uri
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository
import kotlinx.coroutines.flow.flow

class AddNewPlaylistUseCase(private val repository: PlaylistsRepository) {
    operator fun invoke(name: String, description: String, imageUri: Uri?) = flow {
        try {
            repository.addNewPlaylist(name, description, imageUri)
            emit(Pair(true, name))
        } catch (e: Throwable) {
            emit(Pair(false, name))
        }
    }
}