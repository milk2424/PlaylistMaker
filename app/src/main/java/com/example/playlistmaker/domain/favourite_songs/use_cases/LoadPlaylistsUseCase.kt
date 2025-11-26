package com.example.playlistmaker.domain.favourite_songs.use_cases

import com.example.playlistmaker.domain.favourite_songs.model.PlaylistState
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadPlaylistsUseCase(private val repository: PlaylistsRepository) {

    operator fun invoke(): Flow<PlaylistState> = flow {
        emit(PlaylistState.Loading)
        val data = repository.loadPlaylists()
        if (data.isEmpty()) emit(PlaylistState.Empty)
        else emit(PlaylistState.Success(data))
    }

}