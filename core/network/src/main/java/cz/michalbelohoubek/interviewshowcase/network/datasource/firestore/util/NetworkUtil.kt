package cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.util

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import cz.michalbelohoubek.interviewshowcase.common.network.exceptions.UserNotAuthenticatedException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

fun Query.asFlow(): Flow<QuerySnapshot> =
    callbackFlow {
        val callback = addSnapshotListener { querySnapshot, exception ->
            exception?.let {
                trySend(Result.failure(it))
                close(it)
            } ?: trySend(Result.success(querySnapshot!!)) // we know for sure if exception is null, so querySnapshot is not (one of querySnapshot or exception is always not null)
        }
        awaitClose { callback.remove() }
    }.map { it.getOrThrow() }

fun Throwable.shouldSignOut() =
    when (this) {
        is FirebaseFirestoreException -> {
            when (code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED,
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> true
                else -> false
            }
        }
        is UserNotAuthenticatedException -> true
        else -> false
    }