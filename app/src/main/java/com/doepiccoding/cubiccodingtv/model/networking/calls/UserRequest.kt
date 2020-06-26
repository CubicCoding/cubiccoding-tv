package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.model.dtos.LoginRequestPayload
import com.doepiccoding.cubiccodingtv.model.networking.CubicCodingRequestException
import com.doepiccoding.cubiccodingtv.model.networking.RequestErrorType
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager
import com.doepiccoding.cubiccodingtv.model.utils.Constants.AUTHORIZATON_HEADER
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import timber.log.Timber

object UserRequest {

    @WorkerThread
    fun login(username: String, password: String): Boolean {
        Timber.d("Logging in user: $username")
        val response = RequestsManager.cubicCodingManagerApi.login(LoginRequestPayload(username, password)).execute()
        when {
            response.isSuccessful -> {
                if (response.body() != null) {
                    UserPersistedData.isLogged = true
                    //Get cubiccoding's api token
                    response.headers()[AUTHORIZATON_HEADER]?.apply {
                        UserPersistedData.ccToken = this
                        Timber.d("Saved user token: $this")
                    }
                    return true
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
