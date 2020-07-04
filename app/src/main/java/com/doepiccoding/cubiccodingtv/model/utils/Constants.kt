package com.doepiccoding.cubiccodingtv.model.utils

import com.doepiccoding.cubiccodingtv.R

object Constants {

    //================== HTTP Constants ==================
    const val CUBICCODING_MX_URL = "https://www.cubiccoding.mx/"
    const val CUBICCODING_MX_VIDEO_RESOURCES_URL = "https://www.cubiccoding.mx/resources/videos/"
//    val CUBICCODING_MANAGER_URL = "https://cubiccoding-api.herokuapp.com/"
    const val CUBICCODING_MANAGER_URL = "http://192.168.0.13:8080/"
    const val HTTP_WAIT_TIME_IN_SECS = 30L
    const val HTTP_RESOURCE_NOT_FOUND = 404
    const val HTTP_RESOURCE_GONE = 410
    const val HTTP_UNPROCESABLE_ENTITY = 422
    const val HTTP_GONE = 410
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_CONFLICT = 409
    const val AUTHORIZATON_HEADER = "Authorization"

    const val EXPIRATION_YELLOW = "YELLOW"
    const val EXPIRATION_RED = "RED"

    const val ONE_HOUR_IN_MS = 1000 * 60 * 60


    val POOL_OF_COLORS = listOf(
        R.color.timeline_color_1, R.color.timeline_color_2, R.color.timeline_color_3, R.color.timeline_color_4,
        R.color.timeline_color_5, R.color.timeline_color_6, R.color.timeline_color_7, R.color.timeline_color_8, R.color.timeline_color_9)

}
