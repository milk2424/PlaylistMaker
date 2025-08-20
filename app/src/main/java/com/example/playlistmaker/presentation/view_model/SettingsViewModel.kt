package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.interactor.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.interactor.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    companion object {
        fun getFactory(): ViewModelProvider.Factory {
            val sharingInteractor = Creator.provideSharingInteractor()
            val settingsInteractor = Creator.provideSettingsInteractor()
            return viewModelFactory {
                initializer {
                    SettingsViewModel(sharingInteractor, settingsInteractor)
                }
            }
        }
    }

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