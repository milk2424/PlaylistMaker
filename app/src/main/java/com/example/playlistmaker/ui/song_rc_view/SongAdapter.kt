package com.example.playlistmaker.ui.song_rc_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song

class SongAdapter : RecyclerView.Adapter<SongViewHolder>() {
    private val songList: MutableList<Song> = mutableListOf()
    var onClickCallback: ((Song) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int = songList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.songView.setOnClickListener {
            onClickCallback?.invoke(songList[position])
        }
        holder.bind(songList[position])
    }

    fun resetAdapter(songs: List<Song>) {
        songList.clear()
        songList.addAll(songs)
        notifyDataSetChanged()
    }
}