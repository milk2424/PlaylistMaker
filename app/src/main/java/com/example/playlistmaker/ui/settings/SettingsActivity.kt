package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

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
            viewModel.shareApp()
        }
        binding.btnWriteToSupport.setOnClickListener {
            viewModel.openSupport()
        }
        binding.btnUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }

        viewModel.isNightLiveData().observe(this) { themeSettings ->
            binding.themeSwitch.isChecked = themeSettings.isNight
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
    }
}