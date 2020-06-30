package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.model.dtos.LoginRequestPayload
import com.doepiccoding.cubiccodingtv.model.networking.CubicCodingRequestException
import com.doepiccoding.cubiccodingtv.model.networking.RequestErrorType
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager
import com.doepiccoding.cubiccodingtv.model.utils.Constants.AUTHORIZATON_HEADER
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import timber.log.Timber

object GroupsRequest {

    @WorkerThread
    fun getAllGroups(): List<GroupsResponsePayload> {
        val response = RequestsManager.cubicCodingManagerApi.getAllGroups().execute()
        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    return body
                } else {
                    throw CubicCodingRequestException(
                        "Get All groups body is null",
                        RequestErrorType.NULL_BODY,
                        response.code()
                    )
                }
            }
            else -> throw CubicCodingRequestException(
                "Get All groups request failed",
                RequestErrorType.UNSUCCESS,
                response.code()
            )
        }
    }

    @WorkerThread
    fun getExpirationByGroup(groupName: String): List<ExpirationPayload> {
        val response = RequestsManager.cubicCodingManagerApi.getExpirationsByGroupName(groupName).execute()
        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    return body
                } else {
                    throw CubicCodingRequestException(
                        "Get Expirations by group name body is null",
                        RequestErrorType.NULL_BODY,
                        response.code()
                    )
                }
            }
            else -> throw CubicCodingRequestException(
                "Get Expirations by group name request failed",
                RequestErrorType.UNSUCCESS,
                response.code()
            )
        }
    }
}
