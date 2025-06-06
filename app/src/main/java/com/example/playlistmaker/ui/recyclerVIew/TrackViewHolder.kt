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
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(val trackView: View) : RecyclerView.ViewHolder(trackView) {
    private var trackImage: ImageView = trackView.findViewById(R.id.track_image)
    private var trackName: TextView = trackView.findViewById(R.id.track_name)
    private var trackArtistName: TextView = trackView.findViewById(R.id.track_author)
    private var trackTime: TextView = trackView.findViewById(R.id.track_time)
    private var imageRoundedCornersSize: Int = 0

    init {
        imageRoundedCornersSize = transformDpToPx(
            2f,
            trackView.context
        )
        Log.d("MAIN_TAG", "$imageRoundedCornersSize")
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        trackArtistName.text = track.artistName
        trackTime.text = simpleDateFormat.format(track.trackTimeMillis)
        Glide.with(trackView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.no_track_art)
            .centerCrop()
            .transform(RoundedCorners(imageRoundedCornersSize))
            .into(trackImage)

    }

    companion object {
        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    }
}