package com.example.playlistmaker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        setBtnListeners()
    }

    private fun setBtnListeners() {
        val searchBtn = findViewById<Button>(R.id.btn_search)
        val libraryBtn = findViewById<Button>(R.id.btn_library)
        val settingsBtn = findViewById<Button>(R.id.btn_settings)

        val searchBtnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) =
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))

        }
        searchBtn.setOnClickListener(searchBtnClickListener)
        libraryBtn.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}