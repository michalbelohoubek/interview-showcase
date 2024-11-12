package cz.michalbelohoubek.interviewshowcase.data.repository

import cz.michalbelohoubek.interviewshowcase.common.Result
import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import kotlinx.coroutines.flow.Flow

interface CompetitionRepository {

    fun getCompetitions(): Flow<CompetitionsResult>

    fun getCompetition(competitionId: String): Flow<Competition>

    suspend fun addCompetition(competition: Competition): Result

    suspend fun deleteCompetition(competition: Competition): Result

    fun forceUpdate()

    fun invalidate()
}

sealed interface CompetitionsResult {
    data class Success(val competitions: List<Competition>) : CompetitionsResult
    data object Error : CompetitionsResult
}