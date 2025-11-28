package com.example.playlistmaker.data.favourite_songs.impl

import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.favourite_songs.utils.PlaylistMapper
import com.example.playlistmaker.data.player.mapper.PlaylistSongMapper
import com.example.playlistmaker.domain.favourite_songs.repository.PlaylistDataRepository
import kotlinx.coroutines.flow.flow

class PlaylistDataRepositoryImpl(
    private val dao: PlaylistDao,
    private val songsMapper: PlaylistSongMapper,
    private val playlistMapper: PlaylistMapper
) : PlaylistDataRepository {
    override fun loadSongs(playlistId: Int) = flow {
        val songs = dao.getPlaylistSongs(playlistId)
        emit(songs.map { songsMapper.map(it) })
    }

    override fun getPlaylistTime(playlistId: Int): Long {
        val listOfSongsTime = dao.getPlaylistSongsTime(playlistId)
        if (listOfSongsTime.isEmpty()) return 0L
        return listOfSongsTime.sum()
    }

    override fun deleteSongFromPlaylist(playlistId: Int, songId: String) {
        dao.deletePlaylistSong(playlistId, songId)
        val playlist = dao.getPlaylistById(playlistId)
        val playlistSongs = playlistMapper.toSongsIdsList(playlist.songs).toMutableList()
        playlistSongs.remove(songId)
        playlist.songs = playlistMapper.toSongsIdsString(playlistSongs)
        dao.updatePlaylist(playlist)
    }

    override fun loadPlaylistById(playlistId: Int) = flow {
        val playlist = dao.getPlaylistById(playlistId)
        emit(playlistMapper.map(playlist))
    }
}