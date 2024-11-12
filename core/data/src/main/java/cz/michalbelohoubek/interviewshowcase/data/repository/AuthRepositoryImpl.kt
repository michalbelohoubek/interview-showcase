package cz.michalbelohoubek.interviewshowcase.data.repository

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import cz.michalbelohoubek.interviewshowcase.common.NetworkResult.Error
import cz.michalbelohoubek.interviewshowcase.common.NetworkResult.Success
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventProperty
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum
import cz.michalbelohoubek.interviewshowcase.common.network.Dispatcher
import cz.michalbelohoubek.interviewshowcase.common.network.STDispatchers.IO
import cz.michalbelohoubek.interviewshowcase.common.utils.orFalse
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus
import cz.michalbelohoubek.interviewshowcase.core.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private var credentialManager: CredentialManager,
    @Dispatcher(IO) private var ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun getAuthStatus(): Flow<AuthStatus> =
        callbackFlow {
            val authStateListener = FirebaseAuth.IdTokenListener {
                it.currentUser?.let { currentUser ->
                    if (currentUser.isAnonymous) {
                        trySendBlocking(AuthStatus.LOGGED_ANONYMOUSLY)
                    } else {
                        _user.update { User(email = currentUser.email, name = currentUser.displayName) }
                        trySendBlocking(AuthStatus.LOGGED_IN)
                    }
                } ?: run {
                    trySendBlocking(AuthStatus.LOGGED_OUT)
                }
            }
            firebaseAuth.addIdTokenListener(authStateListener)
            awaitClose {
                firebaseAuth.removeIdTokenListener(authStateListener)
            }
        }

    private val _user = MutableStateFlow(firebaseAuth.currentUser?.run { User(email = email, name = displayName) })
    override val user: StateFlow<User?> = _user.asStateFlow()

    override suspend fun signInAnonymously(): SignInAnonymResponse =
        withContext(ioDispatcher) {
            try {
                firebaseAuth
                    .signInAnonymously()
                    .await()
                    .also {
                        val isNewUser = it.additionalUserInfo?.isNewUser ?: false
                        if (isNewUser) {
                            addUserToFirestore()
                        }
                        AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.ANONYMOUS_LOGIN))
                    }.let {
                        SignInAnonymResponse.Success
                    }
            } catch (e: Exception) {
                AnalyticsUtil.logLoginError("anonymous", e.message.orEmpty(), e)
                SignInAnonymResponse.Error(e)
            }
        }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInResponse =
        withContext(ioDispatcher) {
            try {
                firebaseAuth
                    .signInWithCredential(googleCredential)
                    .await()
                    .also {
                        val isNewUser = it.additionalUserInfo?.isNewUser.orFalse()
                        if (isNewUser) {
                            addUserToFirestore()
                        }
                        AnalyticsUtil.login(firebaseAuth.currentUser?.uid.orEmpty(), firebaseAuth.currentUser?.email)
                    }.let {
                        SignInResponse.Success
                    }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AnalyticsUtil.logLoginError("invalid_credentials", e.message.orEmpty(), e)
                SignInResponse.Error.InvalidCredentials(e)
            } catch (e: FirebaseAuthUserCollisionException) {
                AnalyticsUtil.logLoginError("sign_in_firebase", e.message.orEmpty(), e)
                SignInResponse.Error.AccountCollision(googleCredential)
            } catch (e: Exception) {
                SignInResponse.Error.OtherError(e)
            }
        }

    override suspend fun firebaseLinkWithGoogle(googleCredential: AuthCredential): LinkWithGoogleResponse =
        withContext(ioDispatcher) {
            try {
                firebaseAuth.currentUser?.let { currentUser ->
                    currentUser.linkWithCredential(googleCredential).await()?.let { linkedUser ->
                        AnalyticsUtil.login(linkedUser.user?.uid.orEmpty(), linkedUser.user?.email)
                        AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_FROM_DRAWER))
                        LinkWithGoogleResponse.Success
                    } ?: run {
                        AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_FROM_DRAWER_ERROR))
                        LinkWithGoogleResponse.Error(null)
                    }
                } ?: LinkWithGoogleResponse.Error(IllegalStateException("User is not set in Firebase!"))
            } catch (e: FirebaseAuthUserCollisionException) {
                AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_FROM_DRAWER_ACCOUNT_COLLISION))
                LinkWithGoogleResponse.AccountCollisionError(googleCredential)
            } catch (e: Exception) {
                AnalyticsUtil.logLoginError("link_with_firebase", e.message.orEmpty())
                LinkWithGoogleResponse.Error(e)
            }
        }

    private suspend fun addUserToFirestore() {
        firebaseAuth.currentUser?.apply {
            val user = toUser()
            firebaseFirestore.collection("users").document(uid).set(user).await()
        }
    }

    override suspend fun signOut() =
        withContext(ioDispatcher) {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                firebaseAuth.signOut()
                AnalyticsUtil.logout()
                SignOutResponse.Success
            } catch (e: Exception) {
                AnalyticsUtil.logException(e)
                SignOutResponse.Error(e)
            }
        }

    override suspend fun revokeAccess(): RevokeAccessResponse =
        withContext(ioDispatcher) {
            try {
                firebaseAuth.currentUser?.apply {
                    firebaseFirestore.collection("users").document(uid).delete().await()
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                    delete().await()
                }
                RevokeAccessResponse.Success
            } catch (e: Exception) {
                AnalyticsUtil.logException(e)
                RevokeAccessResponse.Error(e)
            }
        }

    override suspend fun deleteUserAccount(): DeleteUserResponse =
        withContext(ioDispatcher) {
            try {
                firebaseAuth.currentUser?.let { currentUser ->
                    suspendCancellableCoroutine { continuation ->
                        try {
                            val path = "/users/${currentUser.uid}"
                            val deleteFn = Firebase.functions.getHttpsCallable("recursiveDelete")
                            deleteFn.call(hashMapOf("path" to path))
                                .addOnSuccessListener {
                                    AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.DELETED_ACCOUNT))
                                    AnalyticsUtil.logout()
                                    AnalyticsUtil.deleteUser()
                                    if (continuation.isActive) {
                                        continuation.resumeWith(Result.success(Success(Unit)))
                                    }
                                }
                                .addOnFailureListener {
                                    if (continuation.isActive) {
                                        continuation.resumeWith(Result.success(Error(it)))
                                    }
                                }
                        } catch (e: Exception) {
                            if (continuation.isActive) {
                                AnalyticsUtil.logException(e)
                                continuation.resumeWith(Result.success(Error(e)))
                            }
                            return@suspendCancellableCoroutine
                        }
                    }
                    currentUser.delete().await()
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                }
                DeleteUserResponse.Success
            } catch (e: Exception) {
                AnalyticsUtil.logException(e)
                DeleteUserResponse.Error(e)
            }
        }
}

fun FirebaseUser.toUser() = mapOf(
    "DISPLAY_NAME" to displayName,
    "EMAIL" to email,
    "PHOTO_URL" to photoUrl?.toString(),
    "CREATED_AT" to FieldValue.serverTimestamp()
)