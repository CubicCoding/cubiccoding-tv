package com.doepiccoding.cubiccodingtv.front.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

fun isActivityAlive(activity: Activity?): Boolean {
    return !(activity == null || activity.isFinishing || activity.isDestroyed)
}

fun isFragmentAlive(fragment: Fragment?): Boolean {
    return fragment != null && fragment.isAdded && isActivityAlive(fragment.activity)
}

/**
 * Sets the common focus listener logic required
 * for navigable views, this logic assumes that
 * the very first element of the view is the focus
 * indicator...
 */
fun setFocusFrameListeners(list: List<View>) {
    list.forEach { view ->
        view.setOnFocusChangeListener { view, hasFocus -> (view as? ViewGroup)?.getChildAt(0)?.visibility = if (hasFocus) View.VISIBLE else View.GONE }
    }
}
