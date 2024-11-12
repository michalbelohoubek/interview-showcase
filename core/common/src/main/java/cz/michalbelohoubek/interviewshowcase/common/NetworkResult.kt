package cz.michalbelohoubek.interviewshowcase.common

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T?) : NetworkResult<T>
    data class Error(val exception: Throwable? = null) : NetworkResult<Nothing>
    object Loading : NetworkResult<Nothing>

    companion object {

        val <T> NetworkResult<T>.asSuccess: Success<T>?
            get() = this as? Success<T>

        val <T> NetworkResult<T>.asError: Error?
            get() = this as? Error

        suspend fun <T> NetworkResult<T>.onSuccess(block: suspend (T) -> Unit): NetworkResult<T> =
            this.apply { asSuccess?.let { it.data?.let { block(it) } } }

        suspend fun <T> NetworkResult<T>.onError(block: suspend () -> Unit): NetworkResult<T> =
            this.apply { asError?.let { block() } }

        val <T> NetworkResult<T>.asResult: Result
            get() = when (this) {
                is Error -> Result.Error(this.exception)
                Loading -> Result.Loading
                is Success -> Result.Success
            }
    }
}

sealed interface Result {
    object Success : Result
    object Loading : Result
    data class Error(val throwable: Throwable?) : Result

    companion object {
        val Result.asSuccess: Success?
            get() = this as? Success

        val Result.asError: Error?
            get() = this as? Error

        suspend fun Result.onSuccess(block: suspend () -> Unit): Result =
            this.apply { asSuccess?.let { block() } }

        suspend fun Result.onError(block: suspend () -> Unit): Result =
            this.apply { asError?.let { block() } }
    }
}

fun <T> Flow<T>.asResult(): Flow<NetworkResult<T>> {
    return this
        .map<T, NetworkResult<T>> {
            NetworkResult.Success(it)
        }
        .onStart { emit(NetworkResult.Loading) }
        .catch {
            Log.e("NetworkResult", it.message, it)
            emit(NetworkResult.Error(it))
        }
}