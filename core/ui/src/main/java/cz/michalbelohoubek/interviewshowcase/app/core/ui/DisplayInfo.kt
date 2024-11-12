package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource

@Composable
fun DisplayInfo(
    snackbarHostState: SnackbarHostState,
    info: Int?,
    onDisplayed: () -> Unit
) {
    info?.let {
        val message = stringResource(it)
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(message)
            onDisplayed()
        }
    }
}