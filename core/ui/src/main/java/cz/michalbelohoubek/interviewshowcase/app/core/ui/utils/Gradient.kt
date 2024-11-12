package cz.michalbelohoubek.interviewshowcase.app.core.ui.utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import kotlin.math.*

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) =
    this.then(
        Modifier.drawBehind {

            // Setting the angle in radians
            val angleRad = angle / 180f * PI

            // Fractional x
            val x = cos(angleRad).toFloat()

            // Fractional y
            val y = sin(angleRad).toFloat()

            // Set the Radius and offset as shown below
            val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
            val offset = center + Offset(x * radius, y * radius)

            // Setting the exact offset
            val exactOffset = Offset(
                x = min(offset.x.coerceAtLeast(0f), size.width),
                y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
            )

            // Draw a rectangle with the above values
            drawRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(size.width, size.height) - exactOffset,
                    end = exactOffset
                ),
                size = size
            )
        }
    )


/**
 * Creates a linear gradient with the provided colors
 * and angle.
 *
 * @param colors Colors of gradient
 * @param stops Offsets to determine how the colors are dispersed throughout
 * the vertical gradient
 * @param tileMode Determines the behavior for how the shader is to fill a region outside
 * its bounds. Defaults to [TileMode.Clamp] to repeat the edge pixels
 * @param angleInDegrees Angle of a gradient in degrees
 * @param useAsCssAngle Determines whether the CSS gradient angle should be used
 * by default cartesian angle is used
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/gradient/linear-gradient">
 *     linear-gradient</a>
 */
@Immutable
class LinearGradient constructor(
    private val colors: List<Color>,
    private val stops: List<Float>? = null,
    private val tileMode: TileMode = TileMode.Clamp,
    angleInDegrees: Float = 0f,
    useAsCssAngle: Boolean = false
) : ShaderBrush() {

    // handle edge cases like: -1235, ...
    private val normalizedAngle: Float = if (useAsCssAngle) {
        ((90 - angleInDegrees) % 360 + 360) % 360
    } else {
        (angleInDegrees % 360 + 360) % 360
    }
    private val angleInRadians: Float = Math.toRadians(normalizedAngle.toDouble()).toFloat()

    override fun createShader(size: Size): Shader {
        val (from, to) = getGradientCoordinates(size = size)

        return LinearGradientShader(
            colors = colors,
            colorStops = stops,
            from = from,
            to = to,
            tileMode = tileMode
        )
    }

    private fun getGradientCoordinates(size: Size): Pair<Offset, Offset> {
        if (size == Size.Zero) {
            return Offset(0f, 0f) to Offset(0f, 0f)
        }
        val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
        val angleBetweenDiagonalAndWidth = acos(size.width / diagonal)
        val angleBetweenDiagonalAndGradientLine =
            if ((normalizedAngle > 90 && normalizedAngle < 180)
                || (normalizedAngle > 270 && normalizedAngle < 360)
            ) {
                PI.toFloat() - angleInRadians - angleBetweenDiagonalAndWidth
            } else {
                angleInRadians - angleBetweenDiagonalAndWidth
            }
        val halfGradientLine = abs(cos(angleBetweenDiagonalAndGradientLine) * diagonal) / 2

        val horizontalOffset = halfGradientLine * cos(angleInRadians)
        val verticalOffset = halfGradientLine * sin(angleInRadians)

        val start = size.center + Offset(-horizontalOffset, verticalOffset)
        val end = size.center + Offset(horizontalOffset, -verticalOffset)

        return start to end
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinearGradient) return false

        if (colors != other.colors) return false
        if (stops != other.stops) return false
        if (normalizedAngle != other.normalizedAngle) return false
        if (tileMode != other.tileMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colors.hashCode()
        result = 31 * result + (stops?.hashCode() ?: 0)
        result = 31 * result + normalizedAngle.hashCode()
        result = 31 * result + tileMode.hashCode()
        return result
    }

    override fun toString(): String {
        return "LinearGradient(colors=$colors, " +
                "stops=$stops, " +
                "angle=$normalizedAngle, " +
                "tileMode=$tileMode)"
    }
}

/**
 * Creates a linear gradient with the provided colors
 * and angle.
 *
 * @param colors Colors of gradient
 * @param stops Offsets to determine how the colors are dispersed throughout
 * the vertical gradient
 * @param tileMode Determines the behavior for how the shader is to fill a region outside
 * its bounds. Defaults to [TileMode.Clamp] to repeat the edge pixels
 * @param angleInDegrees Angle of a gradient in degrees
 * @param useAsCssAngle Determines whether the CSS gradient angle should be used
 * by default cartesian angle is used
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/gradient/linear-gradient">
 *     linear-gradient</a>
 */
@Stable
fun Brush.Companion.linearGradient(
    colors: List<Color>,
    stops: List<Float>? = null,
    tileMode: TileMode = TileMode.Clamp,
    angleInDegrees: Float = 0f,
    useAsCssAngle: Boolean = false
): Brush = LinearGradient(
    colors = colors,
    stops = stops,
    tileMode = tileMode,
    angleInDegrees = angleInDegrees,
    useAsCssAngle = useAsCssAngle,
)

/**
 * Creates a linear gradient with the provided colors
 * and angle.
 *
 * @param colorStops Colors and their offset in the gradient area
 * @param tileMode Determines the behavior for how the shader is to fill a region outside
 * its bounds. Defaults to [TileMode.Clamp] to repeat the edge pixels
 * @param angleInDegrees Angle of a gradient in degrees
 * @param useAsCssAngle Determines whether the CSS gradient angle should be used
 * by default cartesian angle is used
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/gradient/linear-gradient">
 *     linear-gradient</a>
 */
@Stable
fun Brush.Companion.linearGradient(
    vararg colorStops: Pair<Float, Color>,
    tileMode: TileMode = TileMode.Clamp,
    angleInDegrees: Float = 0f,
    useAsCssAngle: Boolean = false
): Brush = LinearGradient(
    colors = List(colorStops.size) { i -> colorStops[i].second },
    stops = List(colorStops.size) { i -> colorStops[i].first },
    tileMode = tileMode,
    angleInDegrees = angleInDegrees,
    useAsCssAngle = useAsCssAngle,
)