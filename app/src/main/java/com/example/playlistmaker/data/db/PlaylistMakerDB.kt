package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavouriteSongsDao
import com.example.playlistmaker.data.db.entity.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract class PlaylistMakerDB : RoomDatabase() {
    abstract fun songsHistoryDao(): FavouriteSongsDao
}