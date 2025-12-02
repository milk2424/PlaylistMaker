package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun openEmail(emailData: EmailData)
    fun shareMessage(message: String)
}