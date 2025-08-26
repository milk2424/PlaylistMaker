package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val isNightMutableLiveData = MutableLiveData(settingsInteractor.getCurrentTheme())

    fun isNightLiveData(): LiveData<ThemeSettings> = isNightMutableLiveData

    fun shareApp() = sharingInteractor.shareApp()

    fun openTerms() = sharingInteractor.openTerms()

    fun openSupport() = sharingInteractor.openSupport()

    fun switchTheme(isNight: Boolean) {
        val themeSettings = ThemeSettings(isNight)
        isNightMutableLiveData.postValue(themeSettings)
        settingsInteractor.switchTheme(themeSettings)
    }

}