package com.doepiccoding.cubiccodingtv.front.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doepiccoding.cubiccodingtv.CubicCodingTVApplication
import com.doepiccoding.cubiccodingtv.R
import timber.log.Timber

fun showFancyToast(context: Context?, message: String?, gravity: Int = (Gravity.BOTTOM)) {
    if (context != null && message?.isNotEmpty() == true) {
        val toastOffset = context.resources.getDimension(R.dimen.fancy_toast_offset).toInt()
        val textView = LayoutInflater.from(context).inflate(R.layout.fancy_toast, null) as TextView
        textView.text = message
        Toast(context).apply {
            view = textView
            setGravity(gravity, 0, toastOffset)
        }.show()
    }
}

fun loadImageCircle(context: Context = CubicCodingTVApplication.instance, url: String?, imageView: ImageView) {
    try {
        Glide.with(context)
            .setDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_user_gray).error(R.drawable.ic_download_error))
            .load(url)
            .apply(RequestOptions.circleCropTransform()).into(imageView)
    } catch (e: Exception) {
        Timber.e(e, "Glide Error loading  image")
    }
}

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun isActivityContextDestroyed(context: Context): Boolean {
    return context is Activity && context.isDestroyed
}


fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboard() {
    val imm = CubicCodingTVApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
}

fun getBadgeResourceIdByRank(rank: Int) = when(rank) {
    1 -> R.drawable.icon_cc_gold
    2 -> R.drawable.icon_cc_silver
    3 -> R.drawable.icon_cc_bronze
    else -> R.drawable.ic_cc_no_bg
}

