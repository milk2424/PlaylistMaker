package com.example.playlistmaker.domain.sharing.repository

import com.example.playlistmaker.domain.sharing.model.EmailData

interface SharingRepository {
    fun getShareLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}