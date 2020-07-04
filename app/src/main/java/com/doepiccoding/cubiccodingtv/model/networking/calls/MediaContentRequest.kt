package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.model.networking.CubicCodingRequestException
import com.doepiccoding.cubiccodingtv.model.networking.RequestErrorType
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager

object MediaContentRequest {

    @WorkerThread
    fun getMediaResources(): List<String> {
        val response = RequestsManager.cubicCodingMXApi.getMediaResources().execute()
        return when {
            response.isSuccessful -> {
                response.body() ?: throw CubicCodingRequestException(
                        "Get media resources response body is null...",
                        RequestErrorType.NULL_BODY,
                        response.code()
                    )
            }
            else -> {
                throw CubicCodingRequestException(
                    "Get media resources request not successful",
                    RequestErrorType.UNSUCCESS,
                    response.code()
                )
            }
        }
    }
}
