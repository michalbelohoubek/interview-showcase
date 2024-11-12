package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients

@Composable
fun STProgressBar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = if (LocalSTGradients.current.isDarkMode) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
        )
    }
}