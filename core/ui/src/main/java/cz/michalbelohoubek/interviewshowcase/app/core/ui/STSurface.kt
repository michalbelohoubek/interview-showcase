package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.*
import cz.michalbelohoubek.interviewshowcase.app.core.ui.utils.linearGradient

@Composable
fun STSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    dragging: Boolean = false,
    elevationLightMode: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    val elevation = (if (LocalSTGradients.current.isDarkMode) 0.dp else elevationLightMode)
    Surface(
        modifier = if (LocalSTGradients.current.isDarkMode) {
            modifier
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        0.0f to if (dragging) MaterialTheme.colors.onSurface.copy(alpha = .3f) else LocalSTGradients.current.surface.colors[0],
                        1.0f to if (dragging) MaterialTheme.colors.onSurface.copy(alpha = .3f) else LocalSTGradients.current.surface.colors[1],
                        angleInDegrees = LocalSTGradients.current.surface.angle,
                    )
                )
        } else {
            modifier
        },
        shape = shape,
        color = if (LocalSTGradients.current.isDarkMode) Color.Transparent else MaterialTheme.colors.surface,
        elevation = elevation
    ) {
        content()
    }
}

@Composable
fun STGradientSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    angle: Float = 0f,
    dragging: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .run {
                if (LocalSTGradients.current.isDarkMode) {
                    background(
                        Brush.linearGradient(
                            0.0f to LocalSTGradients.current.surface.colors[0].copy(alpha = if (dragging) 0.8f else 1f),
                            1.0f to Color.Transparent,
                            angleInDegrees = angle,
                        )
                    )
                } else {
                    background(
                        Brush.linearGradient(
                            0.0f to MaterialTheme.colors.onSurface.copy(alpha = .2f),
                            1.0f to Color.Transparent,
                            angleInDegrees = angle,
                        )
                    )
                }
            },
        shape = shape,
        color = Color.Transparent,
        elevation = 0.dp
    ) {
        content()
    }
}