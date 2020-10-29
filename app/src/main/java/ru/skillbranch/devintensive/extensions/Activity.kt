package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard(){
    val view = this.currentFocus
    if(view != null){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Activity.isKeyboardOpen(): Boolean {
    val rootView: View = this.findViewById(android.R.id.content)
    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)

    val heightDiff = rootView.height - rect.height()
    val maxDiff = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50.0F, this.resources.displayMetrics)
//    Log.d("S_Activity", "$heightDiff > $maxDiff, ${heightDiff > maxDiff}")
    return heightDiff > maxDiff
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardOpen()
}