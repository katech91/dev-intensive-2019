package ru.skillbranch.devintensive.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
    )
}

fun Context.pxToDp(px: Float): Float {
    return px/ TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1.0F,
            Resources.getSystem().displayMetrics
    )
}