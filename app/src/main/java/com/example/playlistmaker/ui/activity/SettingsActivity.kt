package com.example.playlistmaker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onStart() {
        super.onStart()
        val backToMainScreenBtn = findViewById<ImageButton>(R.id.btn_back)
        backToMainScreenBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}