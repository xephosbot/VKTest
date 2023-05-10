package com.xbot.vktest.ui.extensions

import android.widget.TextView
import androidx.core.view.ViewCompat

fun TextView.setCompoundDrawable(
    start: Int = 0,
    end: Int = 0,
    top: Int = 0,
    bottom: Int = 0
) {
    val ltr = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR
    setCompoundDrawablesWithIntrinsicBounds(if (ltr) start else end, top, if (ltr) end else start, bottom)
}