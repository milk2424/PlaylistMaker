package com.example.playlistmaker.ui.library.favourite_songs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerDpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper

class FavouriteSongViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
) {

    private var songImage: ImageView = itemView.findViewById(R.id.track_image)
    private var songName: TextView = itemView.findViewById(R.id.track_name)
    private var songArtistName: TextView = itemView.findViewById(R.id.track_author)
    private var songTime: TextView = itemView.findViewById(R.id.track_time)
    private var imageRoundedCornersSize: Int = 0

    init {
        imageRoundedCornersSize = PlayerDpToPxMapper.map(
            2f, itemView.context
        )
    }

    fun bind(song: Song) {
        songName.text = song.trackName
        songArtistName.text = song.artistName
        songTime.text = PlayerTimeMapper.map(song.trackTimeMillis)
        Glide.with(itemView).load(song.artworkUrl100).placeholder(R.drawable.no_track_art)
            .centerCrop().transform(RoundedCorners(imageRoundedCornersSize)).into(songImage)

    }
}