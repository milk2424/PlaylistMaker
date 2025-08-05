package com.example.playlistmaker.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val themeInteractor = Creator.provideThemeInteractor()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setListeners()
    }

    private fun setListeners() {
        binding.settingsBackBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_course))
            val title = getString(R.string.app_to_share)
            startActivity(Intent.createChooser(shareIntent, title))
        }
        binding.btnWriteToSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.user_email)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.thank_to_dev_body))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.thank_to_dev_subject))

            startActivity(supportIntent)
        }
        binding.btnUserAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(getString(R.string.link_to_user_agreement))
            startActivity(userAgreementIntent)
        }

        binding.themeSwitch.isChecked = themeInteractor.getCurrentTheme()
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            themeInteractor.switchTheme(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}