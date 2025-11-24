package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistSongEntity

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    fun loadPlaylists(): List<PlaylistEntity>

    @Insert
    fun addNewPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists where playlist_id=:id")
    fun getPlaylistById(id: Int): PlaylistEntity

    @Update
    fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT song_id FROM playlist_songs where playlist_id=:id")
    fun getPlaylistSongsIds(id: Int): List<String>

    @Insert(onConflict = REPLACE)
    fun addSongToPlaylist(song: PlaylistSongEntity)
}