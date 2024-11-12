package cz.michalbelohoubek.interviewshowcase.data.repository

import cz.michalbelohoubek.interviewshowcase.common.NetworkResult.Companion.asResult
import cz.michalbelohoubek.interviewshowcase.common.Result
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.network.Dispatcher
import cz.michalbelohoubek.interviewshowcase.common.network.STDispatchers.IO
import cz.michalbelohoubek.interviewshowcase.common.network.di.ApplicationScope
import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import cz.michalbelohoubek.interviewshowcase.data.model.asModel
import cz.michalbelohoubek.interviewshowcase.data.model.asNetworkModel
import cz.michalbelohoubek.interviewshowcase.data.util.DownloadPolicy
import cz.michalbelohoubek.interviewshowcase.network.datasource.CompetitionDataSource
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util.shouldSignOut
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitionRepositoryImpl @Inject constructor(
    private val competitionDataSource: CompetitionDataSource,
    private val authRepository: AuthRepository,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    @Dispatcher(IO) private var ioDispatcher: CoroutineDispatcher
) : CompetitionRepository {

    private val downloadPolicy: MutableStateFlow<DownloadPolicy> = MutableStateFlow(DownloadPolicy.None)
    private val competitions: MutableSharedFlow<CompetitionsResult> = MutableSharedFlow(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCompetitions(): Flow<CompetitionsResult> =
        downloadPolicy.flatMapMerge { policy ->
            when (policy) {
                DownloadPolicy.Cache -> {
                    competitions
                }
                DownloadPolicy.Network -> {
                    coroutineScope.launch {
                        competitionDataSource.getCompetitions()
                            .map { it.map { it.asModel } }
                            .catch { ex ->
                                if (ex.shouldSignOut()) {
                                    when (authRepository.signOut()) {
                                        is SignOutResponse.Error -> {
                                            competitions.emit(CompetitionsResult.Error)
                                        }
                                        SignOutResponse.Success -> {
                                            AnalyticsUtil.logLogout("auto")
                                            invalidate()
                                        }
                                    }
                                } else {
                                    competitions.emit(CompetitionsResult.Error)
                                }
                            }
                            .flowOn(ioDispatcher)
                            .collect {
                                competitions.emit(CompetitionsResult.Success(it))
                                downloadPolicy.update { DownloadPolicy.Cache }
                            }
                    }
                    competitions
                }
                DownloadPolicy.None -> {
                    emptyFlow()
                }
            }
        }

    override fun getCompetition(competitionId: String): Flow<Competition> =
        competitionDataSource
            .getCompetition(competitionId)
            .map { it.asModel }
            .flowOn(ioDispatcher)

    override suspend fun addCompetition(competition: Competition): Result =
        withContext(ioDispatcher) {
            competitionDataSource.insertOrUpdateCompetition(competition.asNetworkModel).asResult
        }

    override suspend fun deleteCompetition(competition: Competition): Result =
        withContext(ioDispatcher) {
            competitionDataSource.deleteCompetition(competition.asNetworkModel).asResult
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun forceUpdate() {
        coroutineScope.coroutineContext.cancelChildren()
        competitions.resetReplayCache()
        downloadPolicy.update { DownloadPolicy.Network }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invalidate() {
        coroutineScope.coroutineContext.cancelChildren()
        competitions.resetReplayCache()
        downloadPolicy.update { DownloadPolicy.None }
    }
}

