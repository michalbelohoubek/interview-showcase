package cz.michalbelohoubek.interviewshowcase.webview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STScaffold

@Composable
fun WebViewScreen(
    viewModel: WebViewViewModel = hiltViewModel()
) {
    STScaffold(
        contentColor = MaterialTheme.colors.onBackground
    ) { padding ->
        WebViewContent(
            modifier = Modifier.padding(padding),
            url = viewModel.url
        )
    }
}

@Composable
fun WebViewContent(
    modifier: Modifier = Modifier,
    url: String
) {
    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            //settings.loadWithOverviewMode = true
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}