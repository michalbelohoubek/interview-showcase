package cz.michalbelohoubek.interviewshowcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.list.DrawerMenuItem
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.navigation.CompetitionsDestination
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.navigation.mainGraph
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.navigation.navigateToSignIn
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.navigation.signInGraph
import cz.michalbelohoubek.interviewshowcase.common.utils.Constants
import cz.michalbelohoubek.interviewshowcase.webview.navigation.navigateToWebView
import cz.michalbelohoubek.interviewshowcase.webview.navigation.webViewGraph

/**
 * Top-level navigation graph
 */
@Composable
fun STNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onExitApp: () -> Unit,
    onUiModeChange: (isDarkMode: Boolean) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = CompetitionsDestination,
        modifier = modifier
    ) {
        signInGraph(
            signedIn = navController::popBackStack,
            onBackClick = onExitApp
        )
        mainGraph(
            loggedOut = {
                navController.navigateToSignIn()
            },
            onAddCompetitionClicked = {
                // Not available for the showcase
            },
            onDrawerMenuItemClicked = { item ->
                when (item) {
                    DrawerMenuItem.PrivacyPolicy -> navController.navigateToWebView(Constants.Urls.PRIVACY_POLICY)
                    DrawerMenuItem.TermsAndConditions -> navController.navigateToWebView(Constants.Urls.TERMS_AND_CONDITIONS)
                }
            },
            onUiModeChange = onUiModeChange,
        )
        webViewGraph(
            onBackClick = navController::popBackStack
        )
    }
}