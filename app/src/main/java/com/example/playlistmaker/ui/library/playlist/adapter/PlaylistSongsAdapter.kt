package com.example.playlistmaker.ui.library.playlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.ui.song_rc_view.SongViewHolder

class PlaylistSongsAdapter(
    private val onItemClick: (Song) -> Unit,
    private val onItemLongClick: (String) -> Unit
) : RecyclerView.Adapter<SongViewHolder>() {

    var songs: List<Song> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
        holder.songView.setOnClickListener { onItemClick(song) }
        holder.songView.setOnLongClickListener {
            onItemLongClick(song.trackId)
            true
        }
    }

    override fun getItemCount() = songs.size
}