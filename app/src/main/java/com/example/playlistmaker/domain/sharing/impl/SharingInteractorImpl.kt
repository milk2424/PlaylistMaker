package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.repository.SharingRepository

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(sharingRepository.getShareLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(sharingRepository.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(sharingRepository.getSupportEmailData())
    }
}