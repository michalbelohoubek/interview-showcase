package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients
import cz.michalbelohoubek.interviewshowcase.app.core.ui.utils.linearGradient

@Composable
fun STScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = Color.Black.copy(0.5F),
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) = Scaffold(
    modifier = if (LocalSTGradients.current.isDarkMode) modifier.background(
        Brush.linearGradient(
            0.0f to LocalSTGradients.current.background.colors[0],
            0.55f to LocalSTGradients.current.background.colors[1],
            1.0f to LocalSTGradients.current.background.colors[2],
            angleInDegrees = LocalSTGradients.current.background.angle,
        )
    ) else modifier,
    scaffoldState = scaffoldState,
    topBar = topBar,
    bottomBar = bottomBar,
    snackbarHost = snackbarHost,
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    isFloatingActionButtonDocked = isFloatingActionButtonDocked,
    drawerContent = drawerContent,
    drawerGesturesEnabled = drawerGesturesEnabled,
    drawerShape = drawerShape,
    drawerElevation = drawerElevation,
    drawerBackgroundColor = drawerBackgroundColor,
    drawerContentColor = drawerContentColor,
    drawerScrimColor = drawerScrimColor,
    backgroundColor = if (LocalSTGradients.current.isDarkMode) Color.Transparent else MaterialTheme.colors.background,
    contentColor = contentColor,
    content =  content
)