package com.doepiccoding.cubiccodingtv.model.dtos

//============================================ RESPONSE ============================================
data class GroupsResponsePayload(val classroomName: String?, val description: String?, val instructor: String?)

//============================================ REQUESTS ============================================
data class LoginRequestPayload(val username: String, val password: String)