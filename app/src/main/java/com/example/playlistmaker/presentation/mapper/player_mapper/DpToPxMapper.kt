package com.example.playlistmaker.presentation.mapper.player_mapper

import android.content.Context
import android.util.TypedValue

object DpToPxMapper {
    fun map(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}