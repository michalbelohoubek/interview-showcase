package cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.AuthScreen
import kotlinx.serialization.Serializable

@Serializable
object SignInDestination

fun NavController.navigateToSignIn(navOptions: NavOptions? = null) {
    navigate(SignInDestination, navOptions)
}

fun NavGraphBuilder.signInGraph(
    signedIn: () -> Unit,
    onBackClick: () -> Unit
) {
    composable<SignInDestination> {
        AuthScreen(
            navigateToApp = signedIn,
            onBackClick = onBackClick
        )
    }
}