package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var isPlaying: Boolean = false
        set(value) {
            field = value
            invalidate()
        }
    private var buttonImageBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    private val pauseImageBitmap: Bitmap?
    private val playImageBitmap: Bitmap?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val tint = getColor(R.styleable.PlaybackButtonView_tint, Color.WHITE)

                val pauseDrawable = getDrawable(R.styleable.PlaybackButtonView_pauseImage)
                pauseDrawable?.setTint(tint)
                pauseImageBitmap = pauseDrawable?.toBitmap()

                val playDrawable = getDrawable(R.styleable.PlaybackButtonView_playImage)
                playDrawable?.setTint(tint)
                playImageBitmap = playDrawable?.toBitmap()

                buttonImageBitmap = pauseImageBitmap
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        buttonImageBitmap = if (isPlaying) pauseImageBitmap else playImageBitmap
        pauseImageBitmap?.let {
            canvas.drawBitmap(buttonImageBitmap ?: pauseImageBitmap, null, imageRect, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }

        return super.onTouchEvent(event)
    }
}

