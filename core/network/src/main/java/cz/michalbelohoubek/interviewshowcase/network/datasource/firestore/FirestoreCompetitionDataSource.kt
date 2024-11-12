package cz.michalbelohoubek.interviewshowcase.network.datasource.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import cz.michalbelohoubek.interviewshowcase.common.NetworkResult
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.network.exceptions.UserNotAuthenticatedException
import cz.michalbelohoubek.interviewshowcase.core.model.Sport
import cz.michalbelohoubek.interviewshowcase.network.datasource.CompetitionDataSource
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util.asFlow
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util.executeBatchOperationForResult
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util.executeSingleOperationForResult
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util.runWithUser
import cz.michalbelohoubek.interviewshowcase.network.model.NetworkCompetition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreCompetitionDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : CompetitionDataSource {

    override suspend fun insertOrUpdateCompetition(competition: NetworkCompetition): NetworkResult<Unit> =
        executeSingleOperationForResult(
            block = {
                db.collection("users")
                    .document(it.uid)
                    .collection("competitions")
                    .document(competition.id!!)
            },
            operation = { set(competition) }
        )

    override suspend fun insertOrUpdateCompetitions(competitions: List<NetworkCompetition>): NetworkResult<Unit> =
        executeBatchOperationForResult(
            block = {
                db.collection("users")
                    .document(it.uid)
                    .collection("competitions")
            },
            data = { reference ->
                val batch = db.batch()
                competitions.forEach { competition ->
                    batch.set(reference.document(competition.id!!), competition)
                }
                batch
            }
        )

    override suspend fun deleteCompetition(competition: NetworkCompetition): NetworkResult<Unit> =
        runWithUser { user ->
            suspendCancellableCoroutine { continuation ->
                try {
                    val path = "/users/${user.uid}/competitions/${competition.id}"
                    val deleteFn = Firebase.functions.getHttpsCallable("recursiveDelete")
                    deleteFn.call(hashMapOf("path" to path))
                        .addOnSuccessListener {
                            if (continuation.isActive) {
                                continuation.resumeWith(Result.success(NetworkResult.Success(Unit)))
                            }
                        }
                        .addOnFailureListener {
                            if (continuation.isActive) {
                                AnalyticsUtil.logException(it)
                                continuation.resumeWith(Result.success(NetworkResult.Error(it)))
                            }
                        }
                } catch (e: Exception) {
                    if (continuation.isActive) {
                        AnalyticsUtil.logException(e)
                        continuation.resumeWith(Result.success(NetworkResult.Error(e)))
                    }
                    return@suspendCancellableCoroutine
                }
            }
        }

    override suspend fun deleteAllCompetitions(): NetworkResult<Unit> {
        return NetworkResult.Error(Throwable("Not supported yet :("))
    }

    override fun getCompetitions(): Flow<List<NetworkCompetition>> =
        Firebase.auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("competitions")
                .asFlow().map {
                    it.toObjects(NetworkCompetition::class.java)
                }
        } ?: flow { throw UserNotAuthenticatedException() }

    override fun getCompetition(competitionId: String): Flow<NetworkCompetition> {
        return Firebase.auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .collection("competitions")
                .whereEqualTo("id", competitionId)
                .asFlow().map {
                    it.toObjects(NetworkCompetition::class.java).first()
                }
        } ?: flow { throw UserNotAuthenticatedException() }
    }

    override fun getCompetitionsFromSport(sport: Sport): Flow<List<NetworkCompetition>> =
        Firebase.auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("competitions")
                .whereEqualTo("sport", sport.getSportId())
                .asFlow().map {
                    it.toObjects(NetworkCompetition::class.java)
                }
        } ?: flow { throw UserNotAuthenticatedException() }
}
