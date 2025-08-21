package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.domain.sharing.repository.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun getShareLink(): String {
        return context.getString(R.string.link_to_course)
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.link_to_user_agreement)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.user_email),
            body = context.getString(R.string.thank_to_dev_body),
            subject = context.getString(R.string.thank_to_dev_subject)
        )
    }
}