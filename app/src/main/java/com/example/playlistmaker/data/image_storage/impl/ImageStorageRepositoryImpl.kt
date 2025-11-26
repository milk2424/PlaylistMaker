package com.example.playlistmaker.data.image_storage.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.image_storage.repository.ImageStorageRepository
import java.io.File
import java.io.FileOutputStream

class ImageStorageRepositoryImpl(private val context: Context) : ImageStorageRepository {
    override fun saveImage(uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLISTS_IMAGES)
        if (!filePath.exists())
            filePath.mkdirs()

        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(filePath, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        return file.absolutePath
    }


    companion object {
        private const val PLAYLISTS_IMAGES = "playlists_images"
    }
}