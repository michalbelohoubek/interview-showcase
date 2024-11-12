package cz.michalbelohoubek.interviewshowcase.app.core.design.fonts

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cz.michalbelohoubek.interviewshowcase.app.core.design.R

private val Avenir = FontFamily(
    Font(R.font.avenir_next_ltpro_condensed, FontWeight.Thin),
    Font(R.font.avenir_next_ltpro_regular, FontWeight.Light),
    Font(R.font.avenir_next_ltpro_medium, FontWeight.Normal),
    Font(R.font.avenir_next_ltpro_demi, FontWeight.SemiBold),
    Font(R.font.avenir_next_ltpro_bold, FontWeight.Bold)
)

internal val AppFont = Avenir