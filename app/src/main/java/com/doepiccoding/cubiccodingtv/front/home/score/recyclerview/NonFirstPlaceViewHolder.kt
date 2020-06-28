package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.View
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.utils.getCachedColor
import com.doepiccoding.cubiccodingtv.front.utils.loadImageCircle
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardItemPayload
import kotlinx.android.synthetic.main.non_first_place_scoreboard_item.view.*


class NonFirstPlaceViewHolder(view: View): ScoreboardViewHolder(view) {

    override fun bind(item: ScoreboardDataItem) {
        val data: ScoreboardItemPayload = item.getData()
        itemView.setOnClickListener {
        }
        itemView.displayName.text = data.displayName
        itemView.classroomValue.text = "${data.currentScore?.toInt() ?: 0}/${data.totalOfferedScore}"
        loadImageCircle(itemView.context, data.avatarUrl, itemView.avatar)

        //Handle ranking
        val rank = data.rank
        itemView.rankValue.text = rank.toString()
        val (icResource, ringColor, visibility) = when (rank) {
            2 -> Triple(R.drawable.icon_cc_silver, getCachedColor(R.color.silver), View.VISIBLE)
            3 -> Triple(R.drawable.icon_cc_bronze, getCachedColor(R.color.bronze), View.VISIBLE)
            else -> Triple(-1, -1, View.INVISIBLE)
        }

        if (visibility == View.VISIBLE) {
            itemView.badge.setImageResource(icResource)
            itemView.avatarRing.background.setTint(ringColor)
        }

        itemView.badge.visibility = visibility
    }
}