package cz.michalbelohoubek.interviewshowcase.app.core.design.colors

import androidx.compose.ui.graphics.Color

private val DarkBackgroundGradient = listOf<Color>(
    dark_darkerBackgroundColor, dark_darkerBackgroundColor, dark_darkerBackgroundColor,
)

private val DarkSurfaceGradient = listOf<Color>(
    dark_surfaceColor, dark_surfaceColor//.copy(alpha = 0.9f)
)

private val DarkButtonGradient = listOf<Color>(
    dark_secondaryLightColor, dark_secondaryDarkColor
)

private val DarkBottomNavItemGradient = listOf<Color>(
    dark_secondaryColor.copy(alpha = 0.0f), dark_secondaryDarkColor.copy(alpha = 0.2f)
)

private val DarkBottomNavItemIndicatorGradient = listOf<Color>(
    dark_secondaryColor.copy(alpha = 1f), dark_secondaryDarkColor.copy(alpha = 1f)
)

private val DarkDividerGradient = listOf<Color>(
    Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.15f)
)

internal val DarkSTGradients = STGradients(
    background = STGradient(
        colors = DarkBackgroundGradient,
        angle = 225f
    ),
    surface = STGradient(
        colors = DarkSurfaceGradient,
        angle = 0f
    ),
    button = STGradient(
        colors = DarkButtonGradient,
        angle = 0f
    ),
    bottomNavItem = STGradient(
        colors = DarkBottomNavItemGradient,
        angle = 90f
    ),
    bottomNavIndicatorItem = STGradient(
        colors = DarkBottomNavItemIndicatorGradient,
        angle = 270f
    ),
    divider = STGradient(
        colors = DarkDividerGradient,
        angle = 0f
    ),
    isDarkMode = true
)