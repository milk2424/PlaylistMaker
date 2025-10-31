package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.SongEntity

@Dao
interface FavouriteSongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToFavourite(song: SongEntity)

    @Delete
    suspend fun removeSongFromFavourite(song: SongEntity)

    @Query("SELECT * FROM favourite_songs")
    suspend fun getFavoriteSongs(): List<SongEntity>

    @Query("SELECT id FROM favourite_songs WHERE id=:id")
    suspend fun isSongFavourite(id: String): String?
}