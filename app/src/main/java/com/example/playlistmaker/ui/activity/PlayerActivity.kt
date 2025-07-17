package com.example.playlistmaker.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.ui.PlayerState.PAUSED
import com.example.playlistmaker.ui.PlayerState.PLAYING
import com.example.playlistmaker.ui.PlayerState.PREPARING
import com.example.playlistmaker.ui.PlayerState.STOPPED
import com.example.playlistmaker.ui.activity.SearchActivity.Companion.PLAY_TRACK
import com.example.playlistmaker.ui.recyclerVIew.TrackViewHolder.Companion.simpleDateFormat
import com.example.playlistmaker.utils.transformDpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var currentTrack: Track? = null
    private val player by lazy { MediaPlayer() }

    private var playerState = STOPPED
    private var isButtonPlayClicked = false
    private var isTrackStoppedBeforeOnPause = false

    private lateinit var playerControlButton: ImageButton
    private lateinit var currentTrackTime: TextView

    private val timeControlRunnable = object : Runnable {
        override fun run() {
            currentTrackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.currentPosition)
            mainHandler.postDelayed(this, UPDATE_CURRENT_TIME_DELAY)
        }
    }
    private val mainHandler by lazy { Handler(mainLooper) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        currentTrack = IntentCompat.getParcelableExtra(intent, PLAY_TRACK, Track::class.java)

        currentTrackTime = findViewById(R.id.current_track_time)

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

        playerControlButton = findViewById(R.id.btn_play)

        player.setDataSource(currentTrack?.previewUrl)
        player.prepare()
        playerState = PREPARING
        player.setOnCompletionListener {
            pausePlayer()
            currentTrackTime.setText(R.string.start_track_time)
        }
        playerControlButton.setOnClickListener {
            if (!debouncePlayButton()) {
                playerControl()
                isTrackStoppedBeforeOnPause = !isTrackStoppedBeforeOnPause
            }
        }

    }

    private fun startPlayer() {
        player.start()
        playerState = PLAYING
        playerControlButton.setImageResource(R.drawable.btn_pause_player)
        mainHandler.post(timeControlRunnable)
    }

    private fun pausePlayer() {
        player.pause()
        playerState = PAUSED
        playerControlButton.setImageResource(R.drawable.btn_start_player)
        mainHandler.removeCallbacks(timeControlRunnable)
    }


    private fun playerControl() {
        when (playerState) {
            PLAYING -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun debouncePlayButton(): Boolean {
        val currentIsButtonPlayClicked = isButtonPlayClicked
        if (!currentIsButtonPlayClicked) {
            isButtonPlayClicked = true
            mainHandler.postDelayed({ isButtonPlayClicked = false }, BUTTON_PLAY_DELAY)
        }
        return currentIsButtonPlayClicked
    }

    override fun onResume() {
        super.onResume()
        if (!isTrackStoppedBeforeOnPause)
            startPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    companion object {
        private const val BUTTON_PLAY_DELAY = 200L
        private const val UPDATE_CURRENT_TIME_DELAY = 300L
    }
}