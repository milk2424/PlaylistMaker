package com.example.playlistmaker.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.ui.activity.SearchActivity.Companion.PLAY_TRACK
import com.example.playlistmaker.ui.recyclerVIew.TrackViewHolder.Companion.simpleDateFormat
import com.example.playlistmaker.utils.transformDpToPx
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {
    private var currentTrack: Track? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        Log.d("PLAYLIST", "onCreate: ${intent.getStringExtra(PLAY_TRACK)} ")
        currentTrack = Gson().fromJson(intent.getStringExtra(PLAY_TRACK), Track::class.java)

        val trackName = findViewById<TextView>(R.id.track_name)
        trackName.text = currentTrack?.trackName

        val artist = findViewById<TextView>(R.id.track_artist)
        artist.text = currentTrack?.artistName

        val duration = findViewById<TextView>(R.id.category_duration_value)
        duration.text = simpleDateFormat.format(currentTrack?.trackTimeMillis)

        if (currentTrack?.collectionName.isNullOrEmpty()) {
            val albumGroup = findViewById<Group>(R.id.album_group)
            albumGroup.visibility = GONE
        } else {
            val album = findViewById<TextView>(R.id.category_album_value)
            album.text = currentTrack?.collectionName
        }

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            val yearGroup = findViewById<Group>(R.id.year_group)
            yearGroup.visibility = GONE
        } else {
            val year = findViewById<TextView>(R.id.category_year_value)
            year.text = currentTrack?.releaseDate!!.substringBefore('-')
        }

        val genre = findViewById<TextView>(R.id.category_genre_value)
        genre.text = currentTrack?.primaryGenreName

        val country = findViewById<TextView>(R.id.category_country_value)
        country.text = currentTrack?.country

        val image = findViewById<ImageView>(R.id.track_image)
        Glide.with(image).load(currentTrack?.getPlayerImageURL()).transform(
            RoundedCorners(
                transformDpToPx(8f, this)
            )
        ).placeholder(R.drawable.player_no_track_image).into(image)

        val btnBack = findViewById<ImageView>(R.id.arrow_back)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}