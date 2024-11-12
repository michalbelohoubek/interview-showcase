package cz.michalbelohoubek.interviewshowcase.app.feature_sign_in

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.SignInStatus.Succcess.asError
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.SignInStatus.Succcess.isLoading
import cz.michalbelohoubek.interviewshowcase.app.core.ui.DisplayError
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STScaffold
import cz.michalbelohoubek.interviewshowcase.app.core.ui.trackScreenView
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.ui.AuthContent
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventProperty
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus.Companion.isAuthenticated

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navigateToApp: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    trackScreenView(AnalyticsUtil.Screens.SIGN_IN)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error = uiState.signInStatus.asError?.error
    val isLoading = uiState.signInStatus.isLoading

    val scaffoldState = rememberScaffoldState()

    BackHandler {
        onBackClick()
    }

    DisplayError(
        snackbarHostState = scaffoldState.snackbarHostState,
        error = error,
        onFinishError = viewModel::clearError
    )

    if (uiState.authStatus.isAuthenticated) {
        LaunchedEffect(Unit) {
            navigateToApp()
        }
    }

    STScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
        val context = LocalContext.current
        AuthContent(
            padding = PaddingValues(16.dp),
            isLoading = isLoading,
            signIn = {
                AnalyticsUtil.logEvent(EventProperty(name = EventPropertyEnum.START_LOGIN))
                viewModel.signInWithCredentialManager(context)
            },
            continueWithoutSignIn = {
                AnalyticsUtil.logEvent(EventProperty(name = EventPropertyEnum.START_ANONYMOUS_LOGIN))
                viewModel.signInAnonymously()
            }
        )
    }
}
