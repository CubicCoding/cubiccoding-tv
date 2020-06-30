package com.doepiccoding.cubiccodingtv.model.networking.apis

import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.model.dtos.LoginRequestPayload
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardResponsePayload
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CubicCodingManagerApi {

    @POST("/login?requires_profile=false")
    fun login(@Body requestBody: LoginRequestPayload): Call<ResponseBody>

    @GET("/api/classrooms")
    fun getAllGroups(@HeaderMap headers: Map<String, String> = RequestsManager.getAuthorizationHeader()): Call<List<GroupsResponsePayload>>

    @GET("/api/scoreboard")
    fun getScoreboard(@Query("email") email: String, @Query("classroomName") classroomName: String, @HeaderMap headers: Map<String, String> = RequestsManager.getAuthorizationHeader()): Call<ScoreboardResponsePayload>

    @GET("/api/v1/admin/student-payments-expiration/group-name/{groupName}")
    fun getExpirationsByGroupName(@Path("groupName") groupName: String, @HeaderMap headers: Map<String, String> = RequestsManager.getAuthorizationHeader()): Call<List<ExpirationPayload>>
}
