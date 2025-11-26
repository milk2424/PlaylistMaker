package com.example.playlistmaker.data.favourite_songs.impl

import android.net.Uri
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.favourite_songs.utils.PlaylistMapper
import com.example.playlistmaker.data.image_storage.repository.ImageStorageRepository
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistsRepository
import com.example.playlistmaker.domain.model.Playlist

class PlaylistRepositoryImpl(
    private val dao: PlaylistDao,
    private val mapper: PlaylistMapper,
    private val imageStorageRepository: ImageStorageRepository
) :
    PlaylistsRepository {
    override fun loadPlaylists(): List<Playlist> {
        return dao.loadPlaylists().map { mapper.map(it) }
    }

    override fun addNewPlaylist(name: String, description: String, imageUri: Uri?) {
        val imagePath = if (imageUri != null) imageStorageRepository.saveImage(imageUri) else ""

        val entity =
            PlaylistEntity(id = 0, name = name, description = description, image = imagePath)

        dao.addNewPlaylist(entity)
    }

    override fun addSongToPlaylist(playlistId: Int, songId: String) {
        val playlist = dao.getPlaylistById(playlistId)
        val playlistSongs = mapper.toSongsIdsList(playlist.songs).toMutableList()
        playlistSongs.add(songId)
        playlist.songs = mapper.toSongsIdsString(playlistSongs)
        dao.updatePlaylist(playlist)
    }
}