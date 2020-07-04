package com.doepiccoding.cubiccodingtv.front.home.timeline.recyclerview

import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.front.utils.getCachedColor
import com.doepiccoding.cubiccodingtv.model.dtos.TimelineStepPayload
import com.doepiccoding.cubiccodingtv.model.utils.Constants
import kotlinx.android.synthetic.main.timeline_step_item.view.*

class TimelineViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(position: Int, timelineStepPayload: TimelineStepPayload, currentProgress: Int) {

        val colorPoolIndex = position % Constants.POOL_OF_COLORS.size
        val color = getCachedColor(Constants.POOL_OF_COLORS[colorPoolIndex])

        itemView.timelineStep.apply {
            text = (position + 1).toString()
            DrawableCompat.setTint(DrawableCompat.wrap(background), color);
        }
        itemView.stepName.apply {
            text = timelineStepPayload.name
            setTextColor(color)
        }
        itemView.stepDescription.text = timelineStepPayload.description
        itemView.stepTopics.text = getTopicsVerticallyOrdered(timelineStepPayload.topics)

        itemView.stepTopicsHeader.setTextColor(color)

        itemView.checkedIcon.visibility = if (position < currentProgress) View.VISIBLE else View.INVISIBLE

        val alpha = if (currentProgress < position) 0.4F else 1F
        itemView.stepInfoHolder.alpha = alpha
        itemView.timelineStep.alpha = alpha
        itemView.todayTopics.visibility = if (position == currentProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun getTopicsVerticallyOrdered(topics: List<String>): String {
        val topicsBuilder = StringBuilder()
        topics.forEach {topic -> topicsBuilder.append("- $topic\n") }
        return topicsBuilder.toString()
    }

}