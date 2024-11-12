package cz.michalbelohoubek.interviewshowcase.app.feature.competition.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.list.CompetitionsRoute
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.list.DrawerMenuItem
import kotlinx.serialization.Serializable

@Serializable
data object CompetitionsDestination

fun NavGraphBuilder.mainGraph(
    loggedOut: () -> Unit,
    onAddCompetitionClicked: () -> Unit,
    onUiModeChange: (isDarkMode: Boolean) -> Unit,
    onDrawerMenuItemClicked: (DrawerMenuItem) -> Unit,
) {

    composable<CompetitionsDestination> {
        CompetitionsRoute(
            loggedOut = loggedOut,
            onAddCompetitionClicked = onAddCompetitionClicked,
            onCompetitionClicked = {
                // Not available for the showcase
            },
            onDrawerMenuItemClicked = onDrawerMenuItemClicked,
            onUiModeChange = onUiModeChange,
        )
    }
}

