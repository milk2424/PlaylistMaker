package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.ThemeInteractor
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {
    override fun getCurrentTheme(): Boolean {
        return themeRepository.getCurrentTheme()
    }

    override fun switchTheme(isDark: Boolean) {
        themeRepository.switchTheme(isDark)
    }
}