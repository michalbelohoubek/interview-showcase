package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.michalbelohoubek.interviewshowcase.app.core.ui.states.UiError

@Composable
fun ErrorScreen(
    paddingValues: PaddingValues,
    error: UiError,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
        .padding(paddingValues)
    ) {
        val message = when (error) {
            is UiError.Plain -> error.string
            is UiError.Res -> stringResource(error.id)
        }
        Text(text = message)
    }
}