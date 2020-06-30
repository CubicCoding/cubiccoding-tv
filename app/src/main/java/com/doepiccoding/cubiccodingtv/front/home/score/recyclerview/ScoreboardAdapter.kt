package com.doepiccoding.cubiccodingtv.front.home.score.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.recyclerview.ScoreboardDataItem.ScoreboardItemType
import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload

class ScoreboardAdapter: RecyclerView.Adapter<ScoreboardViewHolder>() {

    private val scoreboard = mutableListOf<ScoreboardDataItem>()

    override fun getItemViewType(position: Int): Int {
        val item = scoreboard[position]
        return when(item.type) {
            ScoreboardItemType.FIRST_PLACE -> R.layout.first_place_scoreboard_item
            ScoreboardItemType.NON_FIRST_PLACE -> R.layout.non_first_place_scoreboard_item
            ScoreboardItemType.HEADER -> R.layout.header_scoreboard_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType) {
            R.layout.first_place_scoreboard_item -> FirstPlaceViewHolder(view)
            R.layout.non_first_place_scoreboard_item -> NonFirstPlaceViewHolder(view)
            R.layout.header_scoreboard_item -> HeaderViewHolder(view)
            else -> throw IllegalArgumentException("View type doesn't exist for scoreboard recycler view...")
        }
    }
    override fun getItemCount() = scoreboard.size
    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) { holder.bind(scoreboard[position]) }

    fun populateScoreboard(scoreboard: List<ScoreboardDataItem>) {
        this.scoreboard.clear()
        this.scoreboard.addAll(scoreboard)
        notifyDataSetChanged()
    }
}
