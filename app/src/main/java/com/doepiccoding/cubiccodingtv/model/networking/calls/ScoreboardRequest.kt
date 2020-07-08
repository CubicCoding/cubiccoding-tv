package com.doepiccoding.cubiccodingtv.model.networking.calls

import androidx.annotation.WorkerThread
import com.doepiccoding.cubiccodingtv.front.home.score.recyclerview.ScoreboardDataItem
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardAndExpirationDto
import com.doepiccoding.cubiccodingtv.model.networking.CubicCodingRequestException
import com.doepiccoding.cubiccodingtv.model.networking.RequestErrorType
import com.doepiccoding.cubiccodingtv.model.networking.RequestsManager

object ScoreboardRequest {

    data class ScoreboardRequestResult(val tournamentName: String, val tournamentId: Int, val score: List<ScoreboardDataItem>)

    @WorkerThread
    fun getScore(classroomName: String): ScoreboardRequestResult {
        val response = RequestsManager.cubicCodingManagerApi.getScoreboard("xxx", classroomName).execute()
        return when {
            response.isSuccessful -> {
                val secondaries = response.body()?.secondaries ?: emptyList()
                val scoreboardItems = secondaries.map { scoreboardItemPayload ->
                    val type = when (scoreboardItemPayload.rank) {
                        1 -> ScoreboardDataItem.ScoreboardItemType.FIRST_PLACE
                        else -> ScoreboardDataItem.ScoreboardItemType.NON_FIRST_PLACE
                    }
                    ScoreboardDataItem(type, ScoreboardAndExpirationDto(scoreboardItemPayload, null))
                }

                val tournamentName = response.body()?.tournamentInfo?.tournamentName ?: ""
                val tournamentId = response.body()?.tournamentInfo?.id ?: 0

                ScoreboardRequestResult(tournamentName, tournamentId, scoreboardItems)
            }
            else -> {
                throw CubicCodingRequestException(
                    "getScore request not successful",
                    RequestErrorType.UNSUCCESS,
                    response.code()
                )
            }
        }
    }
}
