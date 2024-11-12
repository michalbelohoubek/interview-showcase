package cz.michalbelohoubek.interviewshowcase.app.core.design.colors

import androidx.compose.ui.graphics.Color

/**
 * Gradient classes
 */
data class STGradients(
    val background: STGradient,
    val surface: STGradient,
    val button: STGradient,
    val bottomNavItem: STGradient,
    val bottomNavIndicatorItem: STGradient,
    val divider: STGradient,
    val isDarkMode: Boolean
)

data class STGradient(
    val colors: List<Color>,
    val angle: Float = 0f
)