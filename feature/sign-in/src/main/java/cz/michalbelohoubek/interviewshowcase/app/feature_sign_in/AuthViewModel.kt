package cz.michalbelohoubek.interviewshowcase.app.feature_sign_in

import android.content.Context
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialManagerUtils
import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialsResponse
import cz.michalbelohoubek.interviewshowcase.app.core.auth.GoogleIdTokenResult
import cz.michalbelohoubek.interviewshowcase.app.core.auth.GoogleSignInUtils
import cz.michalbelohoubek.interviewshowcase.app.core.auth.utils.asUiError
import cz.michalbelohoubek.interviewshowcase.app.core.ui.states.UiError
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventProperty
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus
import cz.michalbelohoubek.interviewshowcase.data.repository.AuthRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.SignInAnonymResponse
import cz.michalbelohoubek.interviewshowcase.data.repository.SignInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val authStatus: AuthStatus = AuthStatus.LOADING,
    val signInStatus: SignInStatus? = null,
)

sealed interface SignInStatus {
    data object Succcess : SignInStatus
    data object Loading : SignInStatus
    data class Error(val error: UiError) : SignInStatus

    val SignInStatus?.asError: Error?
        get() = this as? Error

    val SignInStatus?.isLoading: Boolean
        get() = this is Loading
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManagerUtils: CredentialManagerUtils
) : ViewModel() {

    private val _signInStatus = MutableStateFlow<SignInStatus?>(null)

    val uiState: StateFlow<AuthUiState> =
        combine(
            authRepository.getAuthStatus().distinctUntilChanged(), _signInStatus
        ) { authStatus, signInStatus ->
            AuthUiState(authStatus, signInStatus)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthUiState()
        )

    private fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        when (val response = authRepository.firebaseSignInWithGoogle(googleCredential)) {
            is SignInResponse.Success -> {
                _signInStatus.update { SignInStatus.Succcess }
                AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_SUCCESS))
            }
            is SignInResponse.Error -> {
                _signInStatus.update { SignInStatus.Error(response.asUiError()) }
            }
        }
    }

    fun clearError() {
        _signInStatus.update { null }
    }

    fun signInAnonymously() = viewModelScope.launch {
        when (val response = authRepository.signInAnonymously()) {
            is SignInAnonymResponse.Error -> {
                _signInStatus.update { SignInStatus.Error(UiError.Plain(response.error?.message.orEmpty())) }
            }
            SignInAnonymResponse.Success -> {
                AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_SUCCESS))
            }
        }
    }

    fun signInWithCredentialManager(activityContext: Context) = viewModelScope.launch {
        _signInStatus.update { SignInStatus.Loading }
        when (val response = credentialManagerUtils.getCredential(activityContext)) {
            is CredentialsResponse.Success -> {
                handleCredentials(response.credential) { authCredential ->
                    signInWithGoogle(authCredential)
                }
            }
            is CredentialsResponse.Error -> {
                _signInStatus.update { SignInStatus.Error(response.asUiError()) }
            }
        }
    }

    private fun handleCredentials(credential: Credential, onTokenAcquired: (AuthCredential) -> Unit) {
        when (val result = GoogleSignInUtils.getGoogleIdToken(credential)) {
            is GoogleIdTokenResult.Error -> {
                _signInStatus.update { SignInStatus.Error(result.asUiError()) }
            }
            is GoogleIdTokenResult.Success -> onTokenAcquired(result.authCredentials)
        }
    }
}

