package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import com.example.playlistmaker.ui.FragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : FragmentBinding<FragmentSettingsBinding>() {


    private val viewModel: SettingsViewModel by viewModel()


    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {

        binding.btnShareApp.setOnClickListener {
            viewModel.shareApp()
        }
        binding.btnWriteToSupport.setOnClickListener {
            viewModel.openSupport()
        }
        binding.btnUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }

        viewModel.isNightLiveData().observe(viewLifecycleOwner) { themeSettings ->
            binding.themeSwitch.isChecked = themeSettings.isNight
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
    }


}