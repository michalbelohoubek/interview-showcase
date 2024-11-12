package cz.michalbelohoubek.interviewshowcase.app.feature.competition.list

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.FirebaseFirestoreException
import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialManagerUtils
import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialsResponse
import cz.michalbelohoubek.interviewshowcase.app.core.auth.GoogleIdTokenResult
import cz.michalbelohoubek.interviewshowcase.app.core.auth.GoogleSignInUtils
import cz.michalbelohoubek.interviewshowcase.app.core.auth.utils.asUiError
import cz.michalbelohoubek.interviewshowcase.app.core.ui.states.UiError
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.R
import cz.michalbelohoubek.interviewshowcase.common.Result
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventProperty
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus
import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import cz.michalbelohoubek.interviewshowcase.core.model.User
import cz.michalbelohoubek.interviewshowcase.data.repository.AuthRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.CompetitionRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.CompetitionsResult
import cz.michalbelohoubek.interviewshowcase.data.repository.DeleteUserResponse
import cz.michalbelohoubek.interviewshowcase.data.repository.LinkWithGoogleResponse
import cz.michalbelohoubek.interviewshowcase.data.repository.SignInResponse
import cz.michalbelohoubek.interviewshowcase.data.repository.SignOutResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import cz.michalbelohoubek.interviewshowcase.common.utils.combine as combineCustom

@HiltViewModel
class CompetitionsViewModel @Inject constructor(
    private val competitionRepository: CompetitionRepository,
    private val authRepository: AuthRepository,
    private val credentialManagerUtils: CredentialManagerUtils
) : ViewModel() {

    private val _sortedBy: MutableStateFlow<SortBy> = MutableStateFlow(SortBy.CreationDate(SortOrder.DESCENDING))
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage: MutableStateFlow<UiError?> = MutableStateFlow(null)
    private val _infoMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val competitionsUiStateFlow =
        combine(
            competitionRepository.getCompetitions(), _sortedBy
        ) { competitionResult, sortBy ->
            when (competitionResult) {
                CompetitionsResult.Error -> {
                    CompetitionsUiState.Error(UiError.Res(R.string.competitions_error))
                }
                is CompetitionsResult.Success -> {
                    CompetitionsUiState.Success(
                        competitions = sortCompetitions(competitionResult.competitions, sortBy),
                        sortBy = sortBy
                    )
                }
            }
        }.catch {
            if (it is FirebaseFirestoreException) {
                competitionRepository.invalidate()
                //authRepository.signOut()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CompetitionsUiState.Loading
        )

    val uiState: StateFlow<CompetitionsScreenUiState> =
        combineCustom(
            authRepository.getAuthStatus().distinctUntilChanged(),
            authRepository.user,
            competitionsUiStateFlow,
            _isLoading,
            _errorMessage,
            _infoMessage
        ) { authStatus, user, competitions, isLoading, errorMessage, infoMessage ->
            CompetitionsScreenUiState(
                authStatus = authStatus,
                user = user,
                competitionsUiState = competitions,
                isLoading = isLoading,
                errorMessage = errorMessage,
                infoMessage = infoMessage
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CompetitionsScreenUiState()
            )

    private val _dialogToDisplay = MutableStateFlow<CompetitionsDialog>(CompetitionsDialog.None)
    val dialogToDisplay: StateFlow<CompetitionsDialog> = _dialogToDisplay.asStateFlow()

    private fun sortCompetitions(competitions: List<Competition>, sortBy: SortBy): List<Competition> =
        when (sortBy) {
            is SortBy.CreationDate -> {
                when (sortBy.order) {
                    SortOrder.ASCENDING -> competitions.sortedBy { it.createdDate }
                    SortOrder.DESCENDING -> competitions.sortedByDescending { it.createdDate }
                }
            }
            is SortBy.Name -> {
                when (sortBy.order) {
                    SortOrder.ASCENDING -> competitions.sortedBy { it.name.lowercase() }
                    SortOrder.DESCENDING -> competitions.sortedByDescending { it.name.lowercase() }
                }
            }
        }

    fun dismissDialog() {
        _dialogToDisplay.update { CompetitionsDialog.None }
    }

    fun displayDialog(dialog: CompetitionsDialog) {
        _dialogToDisplay.update { dialog }
    }

    fun changeSortedBy(sortedBy: SortBy) {
        _sortedBy.update { sortedBy }
    }

    fun invalidate() {
        competitionRepository.invalidate()
    }

    fun logout() = viewModelScope.launch {
        AnalyticsUtil.logLogout("user")
        competitionRepository.invalidate()
        val response = authRepository.signOut()
        if (response is SignOutResponse.Error) {
            _errorMessage.update { UiError.Res(R.string.sign_out_error) }
        }
    }

    fun deleteAccount() = viewModelScope.launch {
        AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.DELETE_ACCOUNT_CLICK))
        _isLoading.value = true
        val response = authRepository.deleteUserAccount()
        if (response is DeleteUserResponse.Error) {
            _errorMessage.update { UiError.Plain(response.error?.message.orEmpty()) }
        }
        _isLoading.value = false
    }

    fun signInWithCredentialManager(activityContext: Context) = viewModelScope.launch {
        _isLoading.update { true }
        when (val response = credentialManagerUtils.getCredential(activityContext)) {
            is CredentialsResponse.Success -> {
                handleCredentials(response.credential) { authCredential ->
                    linkWithGoogle(authCredential)
                }
            }
            is CredentialsResponse.Error -> _errorMessage.update { response.asUiError() }
        }
        _isLoading.update { false }
    }

    private fun handleCredentials(credential: Credential, onTokenAcquired: (AuthCredential) -> Unit) {
        when (val result = GoogleSignInUtils.getGoogleIdToken(credential)) {
            is GoogleIdTokenResult.Error -> result.asUiError()
            is GoogleIdTokenResult.Success -> onTokenAcquired(result.authCredentials)
        }
    }

    private fun linkWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        when (val response = authRepository.firebaseLinkWithGoogle(credential)) {
            is LinkWithGoogleResponse.AccountCollisionError -> {
                _dialogToDisplay.update { CompetitionsDialog.AccountCollision(response.credential) }
            }
            is LinkWithGoogleResponse.Error -> _errorMessage.update { UiError.Plain(response.error?.message.orEmpty()) }
            LinkWithGoogleResponse.Success -> {}
        }
    }

    fun signInWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        _isLoading.value = true
        when (val response = authRepository.firebaseSignInWithGoogle(credential)) {
            is SignInResponse.Error -> _errorMessage.update { response.asUiError() }
            SignInResponse.Success -> {
                AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_FROM_DRAWER))
                forceUpdateCompetitions()
            }
        }
        _isLoading.value = false
    }

    fun forceUpdateCompetitions() {
        competitionRepository.forceUpdate()
    }

    fun deleteCompetition(competition: Competition) = viewModelScope.launch {
        _isLoading.value = true
        when (val result = competitionRepository.deleteCompetition(competition)) {
            is Result.Error -> {
                _errorMessage.value = UiError.Plain(result.throwable?.message.toString())
                _isLoading.value = false
            }
            Result.Loading -> _isLoading.value = true
            Result.Success -> {
                _isLoading.value = false
                _infoMessage.value = R.string.account_deleted_successfully
            }
        }
    }

    fun clearMessages() {
        _infoMessage.value = null
        _errorMessage.value = null
    }
}

@Stable
data class CompetitionsScreenUiState(
    val competitionsUiState: CompetitionsUiState = CompetitionsUiState.Loading,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiError? = null,
    val infoMessage: Int? = null,
    val authStatus: AuthStatus = AuthStatus.LOADING
)

sealed interface CompetitionsUiState {
    data object Loading : CompetitionsUiState
    data class Error(val error: UiError) : CompetitionsUiState
    data class Success(
        val competitions: List<Competition>,
        val sortBy: SortBy = SortBy.CreationDate(SortOrder.DESCENDING)
    ) : CompetitionsUiState
}

sealed interface CompetitionsDialog {
    data object None : CompetitionsDialog
    data class DeleteCompetition(val competition: Competition) : CompetitionsDialog
    data object DeleteAccount : CompetitionsDialog
    data class AccountCollision(val credential: AuthCredential) : CompetitionsDialog
    data object Upgrade : CompetitionsDialog
    data object Review : CompetitionsDialog
}

sealed class SortBy(@StringRes val stringRes: Int) {
    data class Name(val order: SortOrder) : SortBy(R.string.sort_competitions_by_name)
    data class CreationDate(val order: SortOrder) : SortBy(R.string.sort_competitions_by_last_creation)

    companion object {
        fun values(): List<SortBy> =
            listOf(
                CreationDate(SortOrder.DESCENDING),
                Name(SortOrder.ASCENDING)
            )
    }
}

enum class SortOrder {
    ASCENDING, DESCENDING
}