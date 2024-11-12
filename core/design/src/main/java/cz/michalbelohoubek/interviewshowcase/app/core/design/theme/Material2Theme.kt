package cz.michalbelohoubek.interviewshowcase.app.core.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.DarkSTGradients
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.LightSTGradients
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.STGradients
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_darkerBackgroundColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_primaryColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_primaryDarkColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_secondaryColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_secondaryDarkColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_secondaryTextColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.dark_surfaceColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_darkerBackgroundColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_lighterBackgroundColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_primaryColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_primaryLightColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_primaryTextColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_secondaryColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_secondaryDarkColor
import cz.michalbelohoubek.interviewshowcase.app.core.design.colors.light_secondaryTextColor

/**
 * Light colors
 */
private val LightColors = lightColors(
    primary = light_primaryColor,
    primaryVariant = light_primaryLightColor,
    secondary = light_secondaryColor,
    secondaryVariant = light_secondaryDarkColor,
    onSecondary = light_secondaryTextColor,
    background = light_darkerBackgroundColor,
    surface = light_lighterBackgroundColor,
    error = Color.Red,
    onPrimary = light_primaryTextColor,
    onError = Color.White,
    onBackground = light_primaryTextColor,
    onSurface = light_primaryTextColor,
)

/**
 * Dark colors
 */
private val DarkColors = darkColors(
    primary = dark_primaryColor,
    primaryVariant = dark_primaryDarkColor,
    secondary = dark_secondaryColor,
    secondaryVariant = dark_secondaryDarkColor,
    onSecondary = dark_secondaryTextColor,
    background = dark_darkerBackgroundColor,
    surface = dark_surfaceColor,
    error = Color.Red,
    onPrimary = Color.White,
    onError = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

val LocalSTGradients = staticCompositionLocalOf<STGradients> {
    error("CompositionLocal LocalSTGradients not present")
}

@Composable
fun InterviewShowcaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val gradients = if (darkTheme) DarkSTGradients else LightSTGradients
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(LocalSTGradients provides gradients, content = content)
    }
}