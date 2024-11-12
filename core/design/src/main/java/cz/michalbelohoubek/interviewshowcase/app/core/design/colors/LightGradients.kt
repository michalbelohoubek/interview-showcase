package cz.michalbelohoubek.interviewshowcase.app.core.design.colors

import androidx.compose.ui.graphics.Color

private val LightBackgroundGradient = listOf<Color>(
    light_lighterBackgroundColor, light_lighterBackgroundColor, light_lighterBackgroundColor
)

private val LightSurfaceGradient = listOf<Color>(
    light_surfaceColor, light_surfaceColor//.copy(alpha = 0.9f)
)

private val LightButtonGradient = listOf<Color>(
    light_secondaryLightColor, light_secondaryDarkColor
)

private val LightBottomNavItemGradient = listOf<Color>(
    light_secondaryColor.copy(alpha = 0.0f), light_secondaryDarkColor.copy(alpha = 0.2f)
)

private val LightBottomNavItemIndicatorGradient = listOf<Color>(
    light_secondaryColor.copy(alpha = 1f), light_secondaryDarkColor.copy(alpha = 1f)
)

private val LightDividerGradient = listOf<Color>(
    light_secondaryColor.copy(alpha = 0.0f), light_secondaryDarkColor.copy(alpha = 0.3f), light_secondaryColor.copy(alpha = 0.0f)
)

internal val LightSTGradients = STGradients(
    background = STGradient(
        colors = LightBackgroundGradient,
        angle = 0f
    ),
    surface = STGradient(
        colors = LightSurfaceGradient,
        angle = 0f
    ),
    button = STGradient(
        colors = LightButtonGradient,
        angle = 0f
    ),
    bottomNavItem = STGradient(
        colors = LightBottomNavItemGradient,
        angle = 90f
    ),
    bottomNavIndicatorItem = STGradient(
        colors = LightBottomNavItemIndicatorGradient,
        angle = 270f
    ),
    divider = STGradient(
        colors = LightDividerGradient,
        angle = 0f
    ),
    isDarkMode = false
)