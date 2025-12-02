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

    @Query("SELECT * FROM playlist_songs where playlist_id=:id ORDER BY id DESC ")
    fun getPlaylistSongs(id: Int): List<PlaylistSongEntity>

    @Query("SELECT track_time_millis FROM playlist_songs where playlist_id=:id")
    fun getPlaylistSongsTime(id: Int): List<Long>

    @Insert(onConflict = REPLACE)
    fun addSongToPlaylist(song: PlaylistSongEntity)

    @Query("DELETE  FROM playlist_songs WHERE playlist_id=:playlistId AND song_id=:songId")
    fun deletePlaylistSong(playlistId: Int, songId: String)

    @Query("DELETE from playlist_songs WHERE playlist_id=:playlistId")
    fun deletePlaylistSongs(playlistId: Int)

    @Query("DELETE FROM playlists where playlist_id=:playlistId")
    fun deletePlaylist(playlistId: Int)

    @Query("UPDATE playlists SET playlist_name=:name, playlist_description=:description, image=:image WHERE playlist_id=:playlistId ")
    fun updatePlaylistInfo(playlistId: Int, name: String, description: String, image: String)

}