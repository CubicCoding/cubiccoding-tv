package com.doepiccoding.cubiccodingtv.front.splash.groups_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData

class GroupsAdapter(private val callback: (GroupsResponsePayload) -> Unit): RecyclerView.Adapter<GroupsViewHolder>() {

    private val data = mutableListOf<GroupsResponsePayload>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.groups_layout, parent, false)
        return GroupsViewHolder(view, callback)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setGroups(groups: List<GroupsResponsePayload>) {
        data.clear()
        data.addAll(groups)
        notifyDataSetChanged()
    }
}