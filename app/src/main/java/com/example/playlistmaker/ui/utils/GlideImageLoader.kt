package com.example.playlistmaker.ui.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.mapper.player_mapper.DpToPxMapper
import java.io.File

object GlideImageLoader {
    fun loadPlaylistCornerImage(
        path: String?,
        context: Context,
        image: ImageView,
        corners: Float = 8f
    ) {
        if (!path.isNullOrEmpty()) {
            val imageFile = File(path)
            Glide
                .with(context)
                .load(imageFile)
                .placeholder(R.drawable.no_track_art)
                .transform(
                    CenterCrop(), RoundedCorners(
                        DpToPxMapper.map(corners, context)
                    )
                )
                .into(image)
        } else Glide
            .with(context)
            .load(R.drawable.no_track_art)
            .into(image)
    }

    fun loadPlaylistImage(path: String?, context: Context, image: ImageView) {
        if (!path.isNullOrEmpty()) {
            val imageFile = File(path)
            Glide
                .with(context)
                .load(imageFile)
                .placeholder(R.drawable.no_track_art)
                .transform(
                    CenterCrop()
                )
                .into(image)
        } else Glide
            .with(context)
            .load(R.drawable.no_track_art)
            .into(image)
    }

}