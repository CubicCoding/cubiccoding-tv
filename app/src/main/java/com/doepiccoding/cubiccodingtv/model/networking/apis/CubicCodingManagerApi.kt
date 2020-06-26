package com.doepiccoding.cubiccodingtv.model.networking.apis

import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.model.dtos.LoginRequestPayload
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface CubicCodingManagerApi {

    @POST("/login?requires_profile=false")
    fun login(@Body requestBody: LoginRequestPayload): Call<ResponseBody>

    @GET("/api/classrooms")
    fun getAllGroups(@HeaderMap headers: Map<String, String> = RequestsManager.getAuthorizationHeader()): Call<List<GroupsResponsePayload>>

}
