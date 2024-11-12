package cz.michalbelohoubek.interviewshowcase.network.datasource

import cz.michalbelohoubek.interviewshowcase.common.NetworkResult
import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import cz.michalbelohoubek.interviewshowcase.core.model.Sport
import cz.michalbelohoubek.interviewshowcase.network.model.NetworkCompetition
import kotlinx.coroutines.flow.Flow

interface CompetitionDataSource {

    suspend fun insertOrUpdateCompetition(competition: NetworkCompetition): NetworkResult<Unit>

    suspend fun insertOrUpdateCompetitions(competitions: List<NetworkCompetition>): NetworkResult<Unit>

    suspend fun deleteCompetition(competition: NetworkCompetition): NetworkResult<Unit>

    suspend fun deleteAllCompetitions(): NetworkResult<Unit>

    fun getCompetitions(): Flow<List<NetworkCompetition>>

    fun getCompetition(competitionId: String): Flow<NetworkCompetition>

    fun getCompetitionsFromSport(sport: Sport): Flow<List<NetworkCompetition>>

}