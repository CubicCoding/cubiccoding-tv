package com.doepiccoding.cubiccodingtv.front.utils

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.doepiccoding.cubiccodingtv.CubicCodingTVApplication

val colorsCache = mutableMapOf<Int, Int>()
fun getCachedColor(@ColorRes colorResId: Int): Int {
    return colorsCache.getOrPut(colorResId) {
        ContextCompat.getColor(CubicCodingTVApplication.instance, colorResId)
    }
}