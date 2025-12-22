package com.example.playlistmaker.ui.library.playlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.ui.utils.GlideImageLoader.loadPlaylistCornerImage

class PlaylistAdapter(private val onItemClicked: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists: List<Playlist> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onItemClicked.invoke(playlist)
        }
    }

    override fun getItemCount(): Int = playlists.size
}

class PlaylistViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_library, parent, false)
) {

    private val name = itemView.findViewById<TextView>(R.id.tvPlaylistName)
    private val image = itemView.findViewById<ImageView>(R.id.imgPlaylist)
    private val count = itemView.findViewById<TextView>(R.id.tvSongsCount)

    fun bind(item: Playlist) {
        loadPlaylistCornerImage(item.image, itemView.context, image)
        name.text = item.name
        count.text = itemView.resources.getQuantityString(
            R.plurals.tracks_plurals,
            item.songsCount,
            item.songsCount
        )
    }
}

