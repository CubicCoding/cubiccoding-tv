package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import com.doepiccoding.cubiccodingtv.front.utils.loadImageCircle
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardAndExpirationDto
import kotlinx.android.synthetic.main.non_first_place_scoreboard_item.view.*


class NonFirstPlaceViewHolder(view: View): ScoreboardViewHolder(view) {

    override fun bind(item: ScoreboardDataItem) {
        val data: ScoreboardAndExpirationDto = item.getData()
        val scoreData = data.scoreboardItemPayload
        val expirationData = data.expirationPayload

        itemView.displayName.text = scoreData.displayName
        itemView.classroomValue.text = "${scoreData.currentScore?.toInt() ?: 0}/${scoreData.totalOfferedScore}"
        loadImageCircle(itemView.context, scoreData.avatarUrl, itemView.avatar)

        //Handle ranking
        val rank = scoreData.rank
        itemView.rankValue.text = rank.toString()
        handleExpiration(expirationData)

    }
}
