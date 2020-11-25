package ru.skillbranch.devintensive.ui.custom

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log

class AvatarInitialsDrawable(
    val initials: String,
    val textColor:Int =Color.WHITE,
    val backgroundColor: Int = Color.GRAY
): Drawable() {
    override fun draw(canvas: Canvas) {
        val textPaint = Paint()
        val height = bounds.height()
        val width = bounds.width()

        with(textPaint){
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = (height * 0.3).toFloat()
        }

        canvas.drawColor(backgroundColor)
        val textBound = Rect()
        textPaint.getTextBounds(initials, 0, initials.length, textBound)
        canvas.drawText(initials,(width / 2).toFloat(), (height+textBound.height())/2.toFloat(), textPaint)
    }

    override fun setAlpha(alpha: Int) {
        Log.d("M_AvatarInitialsView","setAlpha")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        Log.d("M_AvatarInitialsView","setColorFilter")
    }

    override fun getOpacity(): Int {
        Log.d("M_AvatarInitialsView","getOpacity")
        return android.graphics.PixelFormat.UNKNOWN
    }

}