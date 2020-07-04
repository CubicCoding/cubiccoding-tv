package com.doepiccoding.cubiccodingtv.model.networking.apis

import com.doepiccoding.cubiccodingtv.model.dtos.*
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

    @GET("/principles_knowledge_timeline.json")
    fun getPrinciplesTimeline(): Call<List<TimelineStepPayload>>

    @GET("/api/classrooms/{classroomName}/timeline-progress")
    fun getClassroomTimelineProgress(@Path("classroomName") classroomName: String, @HeaderMap headers: Map<String, String> = RequestsManager.getAuthorizationHeader()): Call<TimelineProgressPayload>

}
