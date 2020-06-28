package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardItemPayload

class HeaderViewHolder(view: View): ScoreboardViewHolder(view) {

    override fun bind(item: ScoreboardDataItem) {
        val data: ScoreboardItemPayload = item.getData()
        //TODO: Finish Binding for headers is scoreboard...
    }
}