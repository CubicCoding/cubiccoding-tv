package com.doepiccoding.cubiccodingtv.model.dtos

//============================================ RESPONSE ============================================
data class GroupsResponsePayload(val classroomName: String?, val description: String?, val instructor: String?)
data class ScoreboardItemPayload(val rank: Int?, var currentScore: Float?, var totalOfferedScore: Int?, val displayName: String?, val avatarUrl: String?, val email: String?)
data class TournamentInfo(val id: Int?, val tournamentName: String?, val prize: String?)
data class ScoreboardResponsePayload(val tournamentInfo: TournamentInfo?, val secondaries: List<ScoreboardItemPayload>)
//============================================ REQUESTS ============================================
data class LoginRequestPayload(val username: String, val password: String)