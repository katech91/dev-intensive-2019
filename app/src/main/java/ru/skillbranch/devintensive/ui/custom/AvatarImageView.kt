package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import kotlin.math.truncate

class AvatarImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context,attrs,defStyleAttr){

    companion object {
        private const val DEFAULT_BORDER_COLOR: Int = android.graphics.Color.WHITE
        private const val DEFAULT_BORDER_WIDTH: Float = 2F
        private const val DEFAULT_SIZE = 40

        private val bgColors = arrayOf(
                Color.parseColor("#7BC862"),
                Color.parseColor("#E17076"),
                Color.parseColor("#FAA774"),
                Color.parseColor("#6EC9CB"),
                Color.parseColor("#65AADD"),
                Color.parseColor("#A695E7"),
                Color.parseColor("#EE7AAE"),
                Color.parseColor("#2196F3")
        )
    }

    @ColorInt
    private var borderColor = AvatarImageView.DEFAULT_BORDER_COLOR
    @Px
    private var borderWidth = context.dpToPx(AvatarImageView.DEFAULT_BORDER_WIDTH)

    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()

    private var initials: String = "??"
//    private var drawable = AvatarInitialsDrawable(initials)

    private var avatar: String? = null

    init {
        if (attrs != null){
            val ta = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView)
            borderWidth = ta.getDimension(
                    R.styleable.AvatarImageView_aiv_borderWidth,
                    context.dpToPx(AvatarImageView.DEFAULT_BORDER_WIDTH)
            )
            Log.d("M_AvatarImageView","init")

            //borderColor = ta.getColor(R.styleable.AvatarImageView_aiv_borderColor, AvatarImageView.DEFAULT_BORDER_COLOR)
borderColor = Color.RED
            ta.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    override fun onDraw(canvas: Canvas){

        if (avatar != null){
            drawAvatar(canvas)
        }else{
            drawInitials(canvas)
        }

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
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor

        }
    }

    private fun prepareShader(w: Int, h: Int) {
        if (w == 0 || drawable == null) return
        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        Log.d("M_AvatarImageView","prepareShader")
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (avatar != null) prepareShader(width,height)
        Log.d("M_AvatarImageView","setImageBitmap")
    }

    override fun setImageDrawable(drawable: Drawable?) {
        Log.d("M_AvatarImageView","setImageDrawable. $width")
        super.setImageDrawable(drawable)
        if (avatar != null) prepareShader(width, height)

    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (avatar != null) prepareShader(width,height)
        Log.d("M_AvatarImageView","setImageResource")
    }

    private fun drawAvatar(canvas: Canvas){
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    fun setInitials(chars: String?){
        if (!chars.isNullOrEmpty()) {
            initials = chars
        }
    }

    fun setAvatar(text: String){
        avatar = text
    }

    private fun drawInitials(canvas: Canvas) {
        initialsPaint.color = initialsToColor(initials)
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint){
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height* 0.33f
        }

        val offsetY = (initialsPaint.descent() + initialsPaint.ascent())/2
        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY, initialsPaint)
    }

    private fun initialsToColor(letters: String): Int {
        val b = letters[0].toByte()
        val len = bgColors.size
        val d = b/len.toDouble()
        val index = ((d - truncate(d))*len).toInt()
        return bgColors[index]
    }



}