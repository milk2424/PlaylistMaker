package com.example.playlistmaker.ui.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.ui.utils.GlideImageLoader.loadPlaylistImage

class PlayerPlaylistAdapter(private val onItemClicked: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlayerPlaylistViewHolder(parent)

    override fun onBindViewHolder(
        holder: PlayerPlaylistViewHolder, position: Int
    ) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onItemClicked.invoke(playlist)
        }
    }

    override fun getItemCount() = playlists.size
}

class PlayerPlaylistViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_playlist_player, parent, false
    )
) {

    private val image = itemView.findViewById<ImageView>(R.id.imgPlaylist)
    private val name = itemView.findViewById<TextView>(R.id.tvPlaylistName)
    private val count = itemView.findViewById<TextView>(R.id.tvSongsCount)

    fun bind(item: Playlist) {
        loadPlaylistImage(item.image, itemView.context, image)
        name.text = item.name
        count.text = itemView.resources.getQuantityString(
            R.plurals.tracks_plurals,
            item.songsCount,
            item.songsCount
        )
    }
}