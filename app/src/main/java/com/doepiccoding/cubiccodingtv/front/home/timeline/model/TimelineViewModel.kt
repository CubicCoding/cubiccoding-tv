package com.doepiccoding.cubiccodingtv.front.home.timeline.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimelineViewModel: ViewModel() {

    private val timeline: MutableLiveData<TimelineRepository.TimelineInfo> by lazy {
        MutableLiveData<TimelineRepository.TimelineInfo>().also {
            loadTimeline(UserPersistedData.classroomName, UserPersistedData.timelineResource)
        }
    }

    fun getTimeline(): LiveData<TimelineRepository.TimelineInfo>  = timeline

    private fun loadTimeline(classroomName: String, timelineResource: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val timelineInfo = TimelineRepository.getTimelineInfo(classroomName, timelineResource)
            timeline.postValue(timelineInfo)
        }
    }
}
