package cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.michalbelohoubek.interviewshowcase.app.core.design.theme.LocalSTGradients
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STOutlinedButton
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STProgressBar
import cz.michalbelohoubek.interviewshowcase.app.core.ui.SignInButton
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.R

@Composable
fun AuthContent(
    padding: PaddingValues,
    isLoading: Boolean,
    signIn: () -> Unit,
    continueWithoutSignIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .wrapContentSize(align = Alignment.Center),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
                    .size(200.dp),
                painter = painterResource(id = cz.michalbelohoubek.interviewshowcase.app.core.ui.R.drawable.ic_launcher_new_foreground),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center),
                text = stringResource(id = R.string.welcome_prefix),
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center),
                text = stringResource(id = cz.michalbelohoubek.interviewshowcase.app.core.ui.R.string.app_name),
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center),
                text = stringResource(id = R.string.welcome_postfix),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Normal),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp))
            SignInButton(
                modifier = Modifier.fillMaxWidth(),//.padding(horizontal = 40.dp),
                onClick = signIn
            )
            Spacer(modifier = Modifier.size(24.dp))
            STOutlinedButton(
                modifier = Modifier.fillMaxWidth().sizeIn(minHeight = 48.dp),//.padding(horizontal = 40.dp),
                color = if (LocalSTGradients.current.isDarkMode) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant,
                onClick = continueWithoutSignIn,
                text = stringResource(cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.R.string.sign_in_anonymously)
            )
        }
        if (isLoading) {
            STProgressBar()
        }
    }
}