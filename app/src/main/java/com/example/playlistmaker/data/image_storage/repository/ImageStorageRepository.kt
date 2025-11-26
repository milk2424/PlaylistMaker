package com.example.playlistmaker.data.image_storage.repository

import android.net.Uri

interface ImageStorageRepository {

    fun saveImage(uri: Uri): String

}