package cz.michalbelohoubek.interviewshowcase.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.InterviewShowcaseTheme
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STScaffold
import cz.michalbelohoubek.interviewshowcase.core.model.UiMode
import cz.michalbelohoubek.interviewshowcase.navigation.STNavHost

@Composable
fun InterviewShowcaseApp(
    onExitApp: () -> Unit,
    uiMode: UiMode,
    onUiModeChanged: (isDarkMode: Boolean) -> Unit
) {
    val isDarkTheme = when (uiMode) {
        UiMode.DARK -> true
        UiMode.LIGHT -> false
        UiMode.DEFAULT -> isSystemInDarkTheme()
        UiMode.NOT_SET -> return
    }

    val navController = rememberNavController()
    InterviewShowcaseTheme(
        darkTheme = isDarkTheme
    ) {
        STScaffold(
            contentColor = MaterialTheme.colors.onBackground
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                STNavHost(
                    navController = navController,
                    onExitApp = onExitApp,
                    onUiModeChange = onUiModeChanged,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}