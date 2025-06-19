package com.example.playlistmaker.ui.recyclerVIew

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track

class TrackAdapter(val trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    var onClickCallback: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.trackView.setOnClickListener {
            Log.d("TESTGSON", "CLICKED TO VIEW")
            onClickCallback?.invoke(trackList[position])
        }
        holder.bind(trackList[position])
    }
}
