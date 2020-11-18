package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx

class CircleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr){
    companion object{
        private const val DEFAULT_BORDER_COLOR: Int = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH : Float = 2F
    }

    @ColorInt
    private var borderColor = DEFAULT_BORDER_COLOR
    @Px
    private var borderWidth = context.dpToPx(DEFAULT_BORDER_WIDTH)

    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()

    init {
        if (attrs != null){
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = ta.getDimension(
                    R.styleable.CircleImageView_cv_borderWidth,
                    context.dpToPx(DEFAULT_BORDER_WIDTH)
            )
            Log.d("M_CircleImageView","$borderWidth px")

            borderColor = ta.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            ta.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        canvas.drawOval(viewRect.toRectF(), imagePaint)
        val half = (borderWidth/2).toInt()
        viewRect.inset(half, half)
        canvas.drawOval(viewRect.toRectF(),borderPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0) return
        with(viewRect){
            left = 0
            top = 0
            right = w
            bottom = h
        }

        prepareShader(w,h)
    }

    private fun setup() {
        with(borderPaint){
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor

        }
    }

    private fun prepareShader(w: Int, h: Int) {
        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        imagePaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }
}