package com.example.playlistmaker.ui.library.favourite_songs

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.search.model.Song

class FavouriteSongsAdapter : RecyclerView.Adapter<FavouriteSongViewHolder>() {
    var favouriteSongs: List<Song> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavouriteSongViewHolder(parent)

    override fun getItemCount() = favouriteSongs.size

    override fun onBindViewHolder(holder: FavouriteSongViewHolder, position: Int) {
        holder.bind(favouriteSongs[position])
    }
}