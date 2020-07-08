package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import com.doepiccoding.cubiccodingtv.front.utils.loadImageCircle
import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardAndExpirationDto
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardItemPayload
import kotlinx.android.synthetic.main.first_place_scoreboard_item.view.*
import kotlin.math.exp

class FirstPlaceViewHolder(view: View): ScoreboardViewHolder(view) {

    override fun bind(item: ScoreboardDataItem) {
        val data: ScoreboardAndExpirationDto = item.getData()
        val scoreData = data.scoreboardItemPayload
        val expirationData = data.expirationPayload

        itemView.setOnClickListener {

        }
        itemView.displayName.text = scoreData.displayName
        itemView.classroomValue.text = "${scoreData.currentScore?.toInt() ?: 0}/${scoreData.totalOfferedScore}"
        loadImageCircle(itemView.context, scoreData.avatarUrl, itemView.avatar)
        handleExpiration(expirationData)
    }
}