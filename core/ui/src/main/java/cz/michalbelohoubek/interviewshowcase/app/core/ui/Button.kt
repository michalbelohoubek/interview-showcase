package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ButtonShape = RoundedCornerShape(percent = 50)

@Composable
fun STOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    inProgress: Boolean = false,
    color: Color = colors.secondary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.animateContentSize(),
        shape = ButtonShape,
        enabled = enabled,
        onClick = if (inProgress) {
            {}
        } else {
            onClick
        },
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color,
            backgroundColor = Color.Transparent
        ),
        border = BorderStroke(2.dp, color)
    ) {
        Text(
            text = text,
            maxLines = 1,
            color = color
        )
        if (inProgress) {
            Spacer(modifier = Modifier.size(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 1.dp,
                color = color
            )
        }
    }
}

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.sign_in_google),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.secondary
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(
                id = R.drawable.ic_google_logo
            ),
            contentDescription = null
        )
        Text(
            text = text,
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}