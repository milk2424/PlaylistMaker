package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBtnListeners()
    }

    private fun setBtnListeners() {
        val searchBtn = findViewById<Button>(R.id.btn_search)
        val libraryBtn = findViewById<Button>(R.id.btn_library)
        val settingsBtn = findViewById<Button>(R.id.btn_settings)

        val searchBtnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) = showMessage(getString(R.string.btn_search_pressed))
        }
        searchBtn.setOnClickListener(searchBtnClickListener)
        libraryBtn.setOnClickListener {
            showMessage(getString(R.string.btn_library_pressed))
        }
        settingsBtn.setOnClickListener {
            showMessage(getString(R.string.btn_settings_pressed))
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}