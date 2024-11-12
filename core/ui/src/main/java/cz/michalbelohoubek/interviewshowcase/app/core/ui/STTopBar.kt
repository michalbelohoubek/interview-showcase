package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients

@Composable
fun STTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = if (LocalSTGradients.current.isDarkMode) MaterialTheme.colors.primarySurface else MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = if (LocalSTGradients.current.isDarkMode) 0.dp else 16.dp
) = TopAppBar(
    title = title,
    modifier = modifier,
    navigationIcon = navigationIcon,
    actions = actions,
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    elevation = elevation
)