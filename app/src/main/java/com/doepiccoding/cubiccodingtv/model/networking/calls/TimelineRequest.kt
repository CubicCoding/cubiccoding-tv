package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.model.dtos.TimelineProgressPayload
import com.doepiccoding.cubiccodingtv.model.dtos.TimelineStepPayload
import com.doepiccoding.cubiccodingtv.model.networking.CubicCodingRequestException
import com.doepiccoding.cubiccodingtv.model.networking.RequestErrorType
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager


object TimelineRequest {

    @WorkerThread
    fun getStaticResource(timelineResource: String): List<TimelineStepPayload> {
        val response = RequestsManager.cubicCodingManagerApi.getStaticResource(timelineResource).execute()
        return when {
            response.isSuccessful -> {
                response.body() ?: throw CubicCodingRequestException(
                        "Get principles timeline response body is null...",
                        RequestErrorType.NULL_BODY,
                        response.code()
                    )
            }
            else -> {
                throw CubicCodingRequestException(
                    "Get principles timeline request not successful",
                    RequestErrorType.UNSUCCESS,
                    response.code()
                )
            }
        }
    }

    @WorkerThread
    fun getClassroomTimelineProgress(classroomName: String): TimelineProgressPayload {
        val response = RequestsManager.cubicCodingManagerApi.getClassroomTimelineProgress(classroomName).execute()
        return when {
            response.isSuccessful -> {
                response.body() ?: throw CubicCodingRequestException(
                    "Get classroom's timeline progress response body is null...",
                    RequestErrorType.NULL_BODY,
                    response.code()
                )
            }
            else -> {
                throw CubicCodingRequestException(
                    "Get classroom's timeline progress request not successful",
                    RequestErrorType.UNSUCCESS,
                    response.code()
                )
            }
        }
    }
}
