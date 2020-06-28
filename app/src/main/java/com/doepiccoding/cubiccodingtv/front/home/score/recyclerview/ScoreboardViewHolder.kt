package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ScoreboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: ScoreboardDataItem)
}