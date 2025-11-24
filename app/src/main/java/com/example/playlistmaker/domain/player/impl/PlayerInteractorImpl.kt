package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.repository.SongFavouriteStateRepository
import com.example.playlistmaker.domain.search.model.Song

class PlayerInteractorImpl(
    private val songFavouriteStateRepository: SongFavouriteStateRepository,
    private val playlistsRepository: PlaylistsRepository
) : PlayerInteractor {
    override suspend fun addSongToFavourite(song: Song) {
        songFavouriteStateRepository.addSongToFavourite(song)
    }

    override suspend fun removeSongFromFavourite(song: Song) {
        songFavouriteStateRepository.removeSongFromFavourite(song)
    }

    override suspend fun isSongFavourite(id: String): Boolean {
        return songFavouriteStateRepository.isSongFavourite(id)
    }

    override suspend fun addSongToPlaylist(song: Song, playlistId: Int) {
        songFavouriteStateRepository.addSongToPlaylist(song, playlistId)
        playlistsRepository.addSongToPlaylist(playlistId, song.trackId)
    }

    override fun getPlaylistSongsIds(playlistId: Int): List<String> {
        return songFavouriteStateRepository.getPlaylistSongsIds(playlistId)
    }
}