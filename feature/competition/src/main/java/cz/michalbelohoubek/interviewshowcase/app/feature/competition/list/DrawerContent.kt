package cz.michalbelohoubek.interviewshowcase.app.feature.competition.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STDrawerItem
import cz.michalbelohoubek.interviewshowcase.app.core.ui.SignInButton
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.R
import cz.michalbelohoubek.interviewshowcase.common.utils.Constants
import cz.michalbelohoubek.interviewshowcase.common.utils.openEmail
import cz.michalbelohoubek.interviewshowcase.common.utils.openEmailWithSubject

@Composable
fun DrawerContent(
    isAnonymousLogin: Boolean,
    email: String?,
    displayName: String?,
    onUiModeChange: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsAndConditionsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
    ) {
        STDrawerItem(
            verticalPadding = 0.dp
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center),
                    painter = painterResource(id = cz.michalbelohoubek.interviewshowcase.app.core.ui.R.drawable.ic_launcher_new_foreground),
                    contentDescription = null
                )
            }
        }
        if (isAnonymousLogin.not()) {
            STDrawerItem {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    displayName?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    email?.let {
                        Text(
                            text = email,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        } else {
            STDrawerItem {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h5
                )
            }
        }

        val isDarkMode = LocalSTGradients.current.isDarkMode
        STDrawerItem(
            verticalPadding = 4.dp,
            onClick = { onUiModeChange(isDarkMode.not()) }
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Filled.DarkMode,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.drawer_item_dark_mode)
            )
            Switch(
                modifier = Modifier,
                checked = isDarkMode,
                onCheckedChange = onUiModeChange,
            )
        }

        if (isAnonymousLogin.not()) {
            STDrawerItem(
                onClick = onLogoutClick
            ) {
                Icon(
                    modifier = Modifier.size(size = 24.dp),
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
                Text(text = stringResource(R.string.drawer_item_log_out))
            }
        }

        val context = LocalContext.current
        val supportEmailTitle = stringResource(R.string.drawer_support_email_chooser)
        STDrawerItem(
            onClick = {
                context.openEmail(
                    title = supportEmailTitle,
                    senderEmail = Constants.Strings.SUPPORT_EMAIL
                )
            }
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = stringResource(R.string.drawer_item_support))
        }
        val subject = stringResource(R.string.drawer_email_subject_found_bug)
        STDrawerItem(
            onClick = {
                context.openEmailWithSubject(
                    title = supportEmailTitle,
                    senderEmail = Constants.Strings.SUPPORT_EMAIL,
                    subject = subject
                )
            }
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Filled.BugReport,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = stringResource(R.string.drawer_item_report_bug))
        }

        STDrawerItem(
            onClick = onPrivacyClick
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Filled.PrivacyTip,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = stringResource(R.string.drawer_item_privacy_policy))
        }

        STDrawerItem(
            onClick = onTermsAndConditionsClick
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Filled.PrivacyTip,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = stringResource(R.string.drawer_item_terms_conditions))
        }

        if (isAnonymousLogin.not()) {
            STDrawerItem(
                onClick = onDeleteAccountClick
            ) {
                Icon(
                    modifier = Modifier.size(size = 24.dp),
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
                Text(text = stringResource(R.string.drawer_item_delete_account))
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                text = stringResource(R.string.sign_in_advantages_description, formatArgs = arrayOf(
                    stringResource(id = R.string.app_name)))
            )

            SignInButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                onClick = onSignInClick
            )
        }
    }
}