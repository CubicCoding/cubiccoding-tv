package com.doepiccoding.cubiccodingtv.front.splash.groups_recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.front.utils.setFocusFrameListeners
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import kotlinx.android.synthetic.main.groups_layout.view.*

class GroupsViewHolder(view: View, private val callback: (GroupsResponsePayload) -> Unit): RecyclerView.ViewHolder(view) {

    fun bind(data: GroupsResponsePayload) {
        itemView.groupName.text = data.classroomName
        itemView.groupInstructor.text = data.instructor
        itemView.groupDescription.text = data.description

        if (itemView.onFocusChangeListener == null) {
            setFocusFrameListeners(listOf(itemView))
        }

        if (!itemView.hasOnClickListeners()) {
            itemView.setOnClickListener {
                callback(data)
            }
        }
    }

}