package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun STDrawerToolbar(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    onDrawerClick: () -> Unit
) {
    STTopAppBar(
        modifier = modifier,
        title = {
            ToolbarText(text = stringResource(title))
        },
        navigationIcon = {
            IconButton(
                onClick = onDrawerClick
            ) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Drawer Icon")
            }
        }
    )
}