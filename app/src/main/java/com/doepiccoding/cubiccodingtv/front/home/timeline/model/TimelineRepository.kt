package com.doepiccoding.cubiccodingtv.front.home.timeline.model

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.model.dtos.TimelineStepPayload
import com.doepiccoding.cubiccodingtv.model.networking.calls.TimelineRequest
import timber.log.Timber

object TimelineRepository {
    
    @WorkerThread
    @Synchronized
    fun getTimelineInfo(classroomName: String, timelineResource: String): TimelineInfo? {
        Timber.d("Track, get timeline info")
        return try {
            processTimelineInfoFromNetwork(classroomName, timelineResource)
        } catch (e: Exception) {
            Timber.e(e, "Error while getting timeline in repository")
            null
        }
    }

    private fun processTimelineInfoFromNetwork(classroomName: String, timelineResource: String): TimelineInfo {
        val steps = TimelineRequest.getStaticResource(timelineResource)
        val classroomProgress = TimelineRequest.getClassroomTimelineProgress(classroomName)
        val timelineProgress = classroomProgress.timelineProgress
        return TimelineInfo(steps, timelineProgress)
    }

    data class TimelineInfo(val timeline: List<TimelineStepPayload>, val currentProgress: Int)
}