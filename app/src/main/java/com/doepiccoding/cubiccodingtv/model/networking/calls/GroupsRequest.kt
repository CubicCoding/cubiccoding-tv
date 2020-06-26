package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
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
                    UserPersistedData.isLogged = true
                    //Get cubiccoding's api token
                    response.headers()[AUTHORIZATON_HEADER]?.apply {
                        UserPersistedData.ccToken = this
                        Timber.d("Saved user token: $this")
                    }
                    return body
                } else {
                    throw CubicCodingRequestException(
                        "Logging In body is null",
                        RequestErrorType.NULL_BODY,
                        response.code()
                    )
                }
            }
            else -> throw CubicCodingRequestException(
                "Logging In request failed",
                RequestErrorType.UNSUCCESS,
                response.code()
            )
        }
    }
}
