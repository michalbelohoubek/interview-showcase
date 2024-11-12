package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cz.michalbelohoubek.interviewshowcase.app.core.ui.utils.primaryDialogButton
import cz.michalbelohoubek.interviewshowcase.app.core.ui.utils.secondaryDialogButton

enum class DialogStyle {
    DEFAULT, WARNING
}

val DialogStyle.confirmButtonColor: Color
    @Composable
    get() = when (this) {
        DialogStyle.DEFAULT -> MaterialTheme.colors.primaryDialogButton
        DialogStyle.WARNING -> MaterialTheme.colors.error
    }

val DialogStyle.titleColor: Color
    @Composable
    get() = when (this) {
        DialogStyle.DEFAULT -> MaterialTheme.colors.primaryDialogButton
        DialogStyle.WARNING -> MaterialTheme.colors.error
    }

@Composable
fun STAlertDialog(
    modifier: Modifier = Modifier,
    confirmText: String? = null,
    onConfirm: (() -> Unit)? = null,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    title: String? = null,
    message: String? = null,
    style: DialogStyle = DialogStyle.DEFAULT
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismiss?.invoke()
        },
        confirmButton = {
            confirmText?.let {
                STDialogButton(
                    text = confirmText,
                    color = style.confirmButtonColor,
                    onClick = {
                        onConfirm?.invoke()
                    }
                )
            }
        },
        dismissButton = {
            dismissText?.let {
                STDialogButton(
                    text = dismissText,
                    color = MaterialTheme.colors.secondaryDialogButton,
                    onClick = {
                        onDismiss?.invoke()
                    }
                )
            }
        },
        title = {
            title?.let {
                STDialogTitle(
                    text = title,
                    color = style.titleColor
                )
            }
        },
        text = {
            message?.let {
                STDialogMessage(text = message)
            }
        },
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface
    )
}

@Composable
fun STDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.textButtonColors(
            contentColor = color,
        )
    ) {
        Text(
            style = MaterialTheme.typography.h5,
            text = text
        )
    }
}

@Composable
fun STDialogTitle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalTextStyle.current.color
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.h5,
        text = text,
        color = color
    )
}

@Composable
fun STDialogMessage(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.body1,
        text = text
    )
}