package cz.michalbelohoubek.interviewshowcase.app.core.ui.utils

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients

val Colors.primaryDialogButton: Color
    @Composable get() = if (LocalSTGradients.current.isDarkMode) secondary else Color(0xFFD0A700)//E5B800)//CFA600) // Color(0xFFBD9800)

val Colors.secondaryDialogButton: Color
    @Composable get() = if (LocalSTGradients.current.isDarkMode) onSurface.copy(alpha = .7f) else onSurface.copy(alpha = .7f)