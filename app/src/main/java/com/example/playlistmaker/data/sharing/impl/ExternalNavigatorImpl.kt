package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(url: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(intent, context.getString(R.string.app_to_share))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)

    }

    override fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_TEXT, emailData.body)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}