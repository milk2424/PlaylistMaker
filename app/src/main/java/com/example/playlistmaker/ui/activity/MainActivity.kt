package com.example.playlistmaker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private var canItemBeClicked = true
    private val handler by lazy { Handler(mainLooper) }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setBtnListeners()
    }

    private fun setBtnListeners() {
        val searchBtnClickListener: View.OnClickListener = View.OnClickListener {
            if (debounceItemClick()) startActivity(
                Intent(this, SearchActivity::class.java)
            )
        }
        binding.btnSearch.setOnClickListener(searchBtnClickListener)
        binding.btnLibrary.setOnClickListener {
            if (debounceItemClick()) startActivity(Intent(this, LibraryActivity::class.java))
        }
        binding.btnSettings.setOnClickListener {
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