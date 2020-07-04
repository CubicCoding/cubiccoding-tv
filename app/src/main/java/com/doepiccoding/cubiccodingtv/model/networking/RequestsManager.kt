package com.doepiccoding.cubiccodingtv.model.networking

import com.doepiccoding.cubiccodingtv.model.networking.apis.CubicCodingMXApi
import com.doepiccoding.cubiccodingtv.model.networking.apis.CubicCodingManagerApi
import com.doepiccoding.cubiccodingtv.model.utils.Constants
import com.doepiccoding.cubiccodingtv.model.utils.Constants.CUBICCODING_MANAGER_URL
import com.doepiccoding.cubiccodingtv.model.utils.Constants.CUBICCODING_MX_URL
import com.doepiccoding.cubiccodingtv.model.utils.Constants.HTTP_WAIT_TIME_IN_SECS
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RequestsManager {

    lateinit var cubicCodingMXApi: CubicCodingMXApi
    lateinit var cubicCodingManagerApi: CubicCodingManagerApi

    init {
        initializeRetrofit()
    }

    private fun initializeRetrofit() {

        val httpClient = with (OkHttpClient.Builder()) {
            readTimeout(HTTP_WAIT_TIME_IN_SECS, TimeUnit.SECONDS)
            writeTimeout(HTTP_WAIT_TIME_IN_SECS, TimeUnit.SECONDS)
            connectTimeout(HTTP_WAIT_TIME_IN_SECS, TimeUnit.SECONDS)
            build()
        }

        setupManagerApi(httpClient)
        setupMXApi(httpClient)
    }

    private fun setupMXApi(httpClient: OkHttpClient) {
        cubicCodingMXApi = with (Retrofit.Builder()) {
            baseUrl(CUBICCODING_MX_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(httpClient)
            build()
        }.create(CubicCodingMXApi::class.java)
    }

    private fun setupManagerApi(httpClient: OkHttpClient) {
        cubicCodingManagerApi = with (Retrofit.Builder()) {
            baseUrl(CUBICCODING_MANAGER_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(httpClient)
            build()
        }.create(CubicCodingManagerApi::class.java)
    }

    fun getAuthorizationHeader() = Collections.singletonMap(Constants.AUTHORIZATON_HEADER, UserPersistedData.ccToken)
}
