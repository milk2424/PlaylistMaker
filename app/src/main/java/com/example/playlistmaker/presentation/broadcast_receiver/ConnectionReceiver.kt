package com.example.playlistmaker.presentation.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.playlistmaker.R

class ConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ConnectivityManager.CONNECTIVITY_ACTION) return
        if (!isNetworkAvailable(context) && context != null) showNetworkErrorToast(context)
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetwork != null
    }

    private fun showNetworkErrorToast(context: Context) {
        Toast.makeText(
            context,
            context.getString((R.string.network_error_toast)),
            Toast.LENGTH_SHORT
        ).show()
    }
}