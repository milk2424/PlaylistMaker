package com.example.playlistmaker.ui.search

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerDpToPxMapper
import com.example.playlistmaker.presentation.mapper.player_mapper.PlayerTimeMapper

class SongViewHolder(val songView: View) : RecyclerView.ViewHolder(songView) {
    private var songImage: ImageView = songView.findViewById(R.id.track_image)
    private var songName: TextView = songView.findViewById(R.id.track_name)
    private var songArtistName: TextView = songView.findViewById(R.id.track_author)
    private var songTime: TextView = songView.findViewById(R.id.track_time)
    private var imageRoundedCornersSize: Int = 0

    init {
        imageRoundedCornersSize = PlayerDpToPxMapper.map(
            2f, songView.context
        )
        Log.d("MAIN_TAG", "$imageRoundedCornersSize")
    }

    fun bind(song: Song) {
        songName.text = song.trackName
        songArtistName.text = song.artistName
        songTime.text = PlayerTimeMapper.map(song.trackTimeMillis)
        Glide.with(songView).load(song.artworkUrl100).placeholder(R.drawable.no_track_art)
            .centerCrop().transform(RoundedCorners(imageRoundedCornersSize)).into(songImage)

    }
}