package cz.michalbelohoubek.interviewshowcase.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import cz.michalbelohoubek.interviewshowcase.common.NetworkResult
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus
import cz.michalbelohoubek.interviewshowcase.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    fun getAuthStatus(): Flow<AuthStatus>

    val user: StateFlow<User?>

    suspend fun signInAnonymously(): SignInAnonymResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInResponse

    suspend fun firebaseLinkWithGoogle(googleCredential: AuthCredential): LinkWithGoogleResponse

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse

    suspend fun deleteUserAccount(): DeleteUserResponse
}

sealed interface DeleteUserResponse {
    object Success : DeleteUserResponse
    data class Error(val error: Exception?) : DeleteUserResponse
}

sealed interface RevokeAccessResponse {
    object Success : RevokeAccessResponse
    data class Error(val error: Exception?) : RevokeAccessResponse
}

sealed interface SignOutResponse {
    object Success : SignOutResponse
    data class Error(val error: Exception?) : SignOutResponse
}

sealed interface SignInAnonymResponse {
    object Success : SignInAnonymResponse
    data class Error(val error: Exception?) : SignInAnonymResponse
}

sealed interface LinkWithGoogleResponse {
    object Success : LinkWithGoogleResponse
    data class Error(val error: Exception?) : LinkWithGoogleResponse
    data class AccountCollisionError(val credential: AuthCredential) : LinkWithGoogleResponse
}

sealed interface SignInResponse {
    object Success : SignInResponse
    sealed interface Error : SignInResponse {
        data class InvalidCredentials(val error: FirebaseAuthInvalidCredentialsException) : Error
        data class AccountCollision(val credential: AuthCredential) : Error
        data class OtherError(val error: Exception) : Error
    }
}
