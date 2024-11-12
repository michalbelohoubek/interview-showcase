package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cz.michalbelohoubek.interviewshowcase.app.core.ui.utils.primaryDialogButton

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> STHorizontalChips(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    title: String?,
    options: List<T>,
    optionToReadable: @Composable (T) -> String,
    selectedOption: T,
    enabled: Boolean,
    additionalInfo: AnnotatedString? = null,
    onInfoClick: () -> Unit = {},
    onSelectedOption: (T) -> Unit
) {
    var displayInfoDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        title?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Label(
                    modifier = Modifier.padding(paddingValues),
                    text = it
                )
                Icon(
                    modifier = Modifier.clickable {
                        displayInfoDialog = true
                        onInfoClick()
                    },
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information"
                )
            }
        }
        Row(
            modifier = Modifier
                .horizontalScroll(state = rememberScrollState())
                .padding(paddingValues),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { option ->
                val colors = if (option == selectedOption) {
                    ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.secondary),
                        disabledContentColor = MaterialTheme.colors.onSurface.copy(alpha = .5f),
                    )
                } else {
                    ChipDefaults.chipColors()
                }
                Chip(
                    modifier = Modifier.selectable(selected = option == selectedOption) {
                        onSelectedOption(option)
                    },
                    colors = colors,
                    enabled = enabled,
                    onClick = { onSelectedOption(option) }
                ) {
                    Text(text = optionToReadable(option))
                }
            }
        }
    }
    if (displayInfoDialog) {
        additionalInfo?.let {
            InfoDialog(
                spannable = it,
                onDismissDialog = { displayInfoDialog = false }
            )
        }
    }
}

@Composable
private fun InfoDialog(
    spannable: AnnotatedString,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismissDialog()
        },
        buttons = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter),
                onClick = {
                    onDismissDialog()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.primaryDialogButton,
                )
            ) {
                Text(
                    style = MaterialTheme.typography.h5,
                    text = stringResource(R.string.general_ok_understand)
                )
            }
        },
        text = {
            Text(
                style = MaterialTheme.typography.body1,
                text = spannable
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface
    )
}