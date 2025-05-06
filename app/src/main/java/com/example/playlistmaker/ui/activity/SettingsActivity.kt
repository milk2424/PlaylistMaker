package com.example.playlistmaker.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onStart() {
        super.onStart()
        setListeners()
    }

    private fun setListeners() {
        val backToMainScreenBtn = findViewById<TextView>(R.id.settings_back_btn)
        backToMainScreenBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val shareButton = findViewById<Button>(R.id.btn_share_app)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_course))
            val title = getString(R.string.app_to_share)
            startActivity(Intent.createChooser(shareIntent, title)) 
        }

        val supportBtn = findViewById<Button>(R.id.btn_write_to_support)
        supportBtn.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.user_email)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.thank_to_dev_body))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.thank_to_dev_subject))

            startActivity(supportIntent)
        }

        val userAgreementBtn = findViewById<Button>(R.id.btn_user_agreement)
        userAgreementBtn.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(getString(R.string.link_to_user_agreement))
            startActivity(userAgreementIntent)
        }
    }
}