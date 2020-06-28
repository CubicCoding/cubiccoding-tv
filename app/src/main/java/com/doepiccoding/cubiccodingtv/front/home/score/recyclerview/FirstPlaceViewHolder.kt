package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import com.doepiccoding.cubiccodingtv.front.utils.loadImageCircle
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardItemPayload
import kotlinx.android.synthetic.main.first_place_scoreboard_item.view.*

class FirstPlaceViewHolder(view: View): ScoreboardViewHolder(view) {

    override fun bind(item: ScoreboardDataItem) {
        val data: ScoreboardItemPayload = item.getData()

        itemView.setOnClickListener {

        }
        itemView.displayName.text = data.displayName
        itemView.classroomValue.text = "${data.currentScore?.toInt() ?: 0}/${data.totalOfferedScore}"
        loadImageCircle(itemView.context, data.avatarUrl, itemView.avatar)
    }
}