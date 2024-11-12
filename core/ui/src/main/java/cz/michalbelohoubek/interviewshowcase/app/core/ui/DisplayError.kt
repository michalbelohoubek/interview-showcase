package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import cz.michalbelohoubek.interviewshowcase.app.core.ui.states.UiError

@Composable
fun DisplayError(
    snackbarHostState: SnackbarHostState,
    error: UiError?,
    onFinishError: () -> Unit
) {
    error?.let {
        val errorString = when (it) {
            is UiError.Plain -> it.string
            is UiError.Res -> stringResource(it.id)
        }
        LaunchedEffect(errorString) {
            snackbarHostState.showSnackbar(errorString)
            onFinishError()
        }
    }
}