package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("playlist_id")
    val id: Int,
    @ColumnInfo("playlist_name")
    val name: String,
    @ColumnInfo("playlist_description")
    val description: String,
    @ColumnInfo("songs_ids")
    var songs: String = "[]",
    @ColumnInfo("image")
    val image: String?
)
