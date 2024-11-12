package cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import cz.michalbelohoubek.interviewshowcase.common.NetworkResult
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import kotlinx.coroutines.suspendCancellableCoroutine

internal suspend fun <T> runWithUser(block: suspend (FirebaseUser) -> (NetworkResult<T>)): NetworkResult<T> =
    FirebaseAuth.getInstance().currentUser?.let {
        try {
            block(it)
        } catch (error: Exception) {
            AnalyticsUtil.logException(error)
            NetworkResult.Error(error)
        }
    } ?: run {
        val exception = Throwable("User is not signed in!")
        AnalyticsUtil.logException(exception)
        NetworkResult.Error(exception)
    }

internal suspend fun executeSingleOperationForResult(block: (user: FirebaseUser) -> DocumentReference, operation: DocumentReference.() -> Unit): NetworkResult<Unit> =
    runWithUser { user ->
        suspendCancellableCoroutine { continuation ->
            try {
                val documentRef = block(user)
                documentRef.addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        if (continuation.isActive) {
                            continuation.resumeWith(Result.success(NetworkResult.Error(error)))
                        }
                        return@addSnapshotListener
                    } else if (continuation.isActive) {
                        continuation.resumeWith(Result.success(NetworkResult.Success(Unit)))
                    }
                    return@addSnapshotListener
                }
                documentRef.operation()
            } catch (e: Exception) {
                if (continuation.isActive) {
                    AnalyticsUtil.logException(e)
                    continuation.resumeWith(Result.success(NetworkResult.Error(e)))
                }
                return@suspendCancellableCoroutine
            }
        }
    }

internal suspend fun executeBatchOperationForResult(
    block: (user: FirebaseUser) -> CollectionReference,
    data: (CollectionReference) -> WriteBatch
): NetworkResult<Unit> =
    runWithUser { user ->
        suspendCancellableCoroutine { continuation ->
            try {
                val collectionRef = block(user)
                collectionRef.addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        if (continuation.isActive) {
                            AnalyticsUtil.logException(error)
                            continuation.resumeWith(Result.success(NetworkResult.Error(error)))
                        }
                        return@addSnapshotListener
                    }
                    if (continuation.isActive) {
                        continuation.resumeWith(Result.success(NetworkResult.Success(Unit)))
                    }
                    return@addSnapshotListener
                }
                data(collectionRef).commit()
            } catch (e: Exception) {
                if (continuation.isActive) {
                    AnalyticsUtil.logException(e)
                    continuation.resumeWith(Result.success(NetworkResult.Error(e)))
                }
                return@suspendCancellableCoroutine
            }
        }
    }