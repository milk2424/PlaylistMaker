package com.example.playlistmaker.ui.recyclerVIew

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.utils.transformDpToPx

class TrackViewHolder(val trackView: View) : RecyclerView.ViewHolder(trackView) {
    private var trackImage: ImageView
    private var trackName: TextView
    private var trackArtistName: TextView
    private var trackTime: TextView
    private var imageRoundedCornersSize: Int = 0

    fun bind(track: Track) {
        trackName.text = track.trackName
        trackArtistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(trackView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.no_track_art)
            .centerCrop()
            .transform(RoundedCorners(imageRoundedCornersSize))
            .into(trackImage)

    }

    init {
        trackImage = trackView.findViewById(R.id.track_image)
        trackName = trackView.findViewById(R.id.track_name)
        trackArtistName = trackView.findViewById(R.id.track_author)
        trackTime = trackView.findViewById(R.id.track_time)
        imageRoundedCornersSize = transformDpToPx(
            2f,
            trackView.context
        )
        Log.d("MAIN_TAG", "$imageRoundedCornersSize")
    }


}