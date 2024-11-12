package cz.michalbelohoubek.interviewshowcase.app.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    @StringRes iconRes: Int? = null,
    action: (() -> Unit)? = null,
    actionImage: ImageVector? = null,
    onClick: () -> Unit,
) {
    STCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            iconRes?.let {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = it),
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.h5)
                Text(text = subtitle, style = MaterialTheme.typography.body2)
            }
            action?.let {
                IconButton(onClick = action) {
                    actionImage?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun STCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val finalModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable { onClick() }
    } else {
        modifier.fillMaxWidth()
    }
    STSurface(
        modifier = finalModifier
    ) {
        content()
    }
}