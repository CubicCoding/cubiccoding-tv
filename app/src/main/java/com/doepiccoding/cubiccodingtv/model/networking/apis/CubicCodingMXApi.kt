package com.doepiccoding.cubiccodingtv.model.networking.apis

import retrofit2.Call
import retrofit2.http.GET

interface CubicCodingMXApi {

    @GET("/resources/resources.php")
    fun getMediaResources(): Call<List<String>>
}
