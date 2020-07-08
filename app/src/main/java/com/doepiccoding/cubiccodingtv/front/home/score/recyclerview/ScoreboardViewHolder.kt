package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.utils.getCachedColor
import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload
import com.doepiccoding.cubiccodingtv.model.utils.Constants.EXPIRATION_RED
import com.doepiccoding.cubiccodingtv.model.utils.Constants.EXPIRATION_YELLOW


abstract class ScoreboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: ScoreboardDataItem)

    protected fun handleExpiration(expirationPayload: ExpirationPayload?) {
        val view = itemView.findViewById<View>(R.id.availability)

        val wrappedDrawable = DrawableCompat.wrap(view.background.mutate())
        when(expirationPayload?.status) {
            EXPIRATION_RED -> DrawableCompat.setTint(wrappedDrawable, getCachedColor(R.color.error_red))
            EXPIRATION_YELLOW -> DrawableCompat.setTint(wrappedDrawable, getCachedColor(R.color.yellow))
            else -> DrawableCompat.setTint(wrappedDrawable, getCachedColor(R.color.green_right_answer))
        }
    }
}