package cz.michalbelohoubek.interviewshowcase.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.michalbelohoubek.interviewshowcase.webview.WebViewScreen

fun NavController.navigateToWebView(url: String) {
    navigate(WebViewDestination(url))
}

fun NavGraphBuilder.webViewGraph(
    onBackClick: () -> Unit,
) {
    composable<WebViewDestination>{
        WebViewScreen()
    }
}