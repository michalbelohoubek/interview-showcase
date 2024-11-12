package cz.michalbelohoubek.interviewshowcase.app.core.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil

// I don't think PascalCase would suit this method, as it suggests an on-screen element
@SuppressLint("ComposableNaming")
@Composable
fun trackScreenView(name: String) {
    DisposableEffect(Unit){
        AnalyticsUtil.logScreen(name)
        onDispose {  }
    }
}