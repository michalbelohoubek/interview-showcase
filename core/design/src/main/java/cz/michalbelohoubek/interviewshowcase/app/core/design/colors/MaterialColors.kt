package cz.michalbelohoubek.interviewshowcase.app.core.design.colors

import androidx.compose.ui.graphics.Color

private val primaryColor = Color(0xFF1F1F25)
private val primaryDarkColor = Color(0xFF0F0F15)

/**
 * Light colors
 */
internal val light_lighterBackgroundColor = Color(0xFFF0F0F5)
internal val light_darkerBackgroundColor = Color(0xFFF0F0F5)
internal val light_surfaceColor = Color(0xFFF0F0F5)

internal val light_primaryColor = primaryColor
internal val light_primaryLightColor = Color(0xFF3A3A45)
internal val light_primaryDarkColor = primaryDarkColor

internal val light_secondaryColor = Color(0xFFFFCC00)
internal val light_secondaryLightColor = Color(0xFFFFCC00)
internal val light_secondaryDarkColor = Color(0xFFE0AD00)

internal val light_primaryTextColor = Color(0xFF444444)
internal val light_secondaryTextColor = Color(0xFF555555)

/**
 * Dark colors
 */
internal val dark_lighterBackgroundColor = primaryColor
internal val dark_darkerBackgroundColor = primaryDarkColor
internal val dark_surfaceColor = Color(0xFF1D1C2B)

internal val dark_primaryColor = dark_lighterBackgroundColor
internal val dark_primaryLightColor = dark_lighterBackgroundColor
internal val dark_primaryDarkColor = dark_darkerBackgroundColor

internal val dark_secondaryColor = Color(0xFFFFD700)
internal val dark_secondaryLightColor = Color(0xFFFFD700)
internal val dark_secondaryDarkColor = Color(0xFFB57500)

internal val dark_primaryTextColor = Color(0xFFffffff)
internal val dark_secondaryTextColor = Color(0xFF333333)