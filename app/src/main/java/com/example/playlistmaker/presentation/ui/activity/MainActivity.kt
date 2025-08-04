package com.example.playlistmaker.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    private var canItemBeClicked = true
    private val handler by lazy { Handler(mainLooper) }
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
            override fun onClick(v: View?) = if (debounceItemClick()) startActivity(
                Intent(
                    this@MainActivity,
                    SearchActivity::class.java
                )
            ) else {
            }
        }
        searchBtn.setOnClickListener(searchBtnClickListener)
        libraryBtn.setOnClickListener {
            if (debounceItemClick()) startActivity(Intent(this, LibraryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            if (debounceItemClick()) startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun debounceItemClick(): Boolean {
        val currentCanItemBeClicked = canItemBeClicked
        if (currentCanItemBeClicked) {
            canItemBeClicked = false
            handler.postDelayed({ canItemBeClicked = true }, DEBOUNCE_ITEM_CLICK_DELAY)
        }
        return currentCanItemBeClicked
    }

    companion object {
        private const val DEBOUNCE_ITEM_CLICK_DELAY = 400L
    }
}