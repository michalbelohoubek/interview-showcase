package cz.michalbelohoubek.interviewshowcase.app.core.ui.model_converters

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

val TextUnit.convertToDp: Dp
    @Composable
    get() = with(LocalDensity.current) { this@convertToDp.toDp() }

val Dp.convertToSp: TextUnit
    @Composable
    get() = with(LocalDensity.current) { this@convertToSp.toSp() }