package com.example.playlistmaker.presentation.ui.recycler_vIew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Song

class TrackAdapter(private val trackList: List<Song>) : RecyclerView.Adapter<TrackViewHolder>() {

    var onClickCallback: ((Song) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.trackView.setOnClickListener {
            onClickCallback?.invoke(trackList[position])
        }
        holder.bind(trackList[position])
    }
}
