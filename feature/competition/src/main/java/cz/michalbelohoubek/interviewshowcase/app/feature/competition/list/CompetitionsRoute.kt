package cz.michalbelohoubek.interviewshowcase.app.feature.competition.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.michalbelohoubek.interviewshowcase.app.core.ui.CardItem
import cz.michalbelohoubek.interviewshowcase.app.core.ui.DialogStyle
import cz.michalbelohoubek.interviewshowcase.app.core.ui.DisplayError
import cz.michalbelohoubek.interviewshowcase.app.core.ui.DisplayInfo
import cz.michalbelohoubek.interviewshowcase.app.core.ui.EmptyScreen
import cz.michalbelohoubek.interviewshowcase.app.core.ui.ErrorScreen
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STAlertDialog
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STDrawerToolbar
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STHorizontalChips
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STProgressBar
import cz.michalbelohoubek.interviewshowcase.app.core.ui.STScaffold
import cz.michalbelohoubek.interviewshowcase.app.core.ui.model_converters.iconResource
import cz.michalbelohoubek.interviewshowcase.app.core.ui.model_converters.toReadable
import cz.michalbelohoubek.interviewshowcase.app.core.ui.trackScreenView
import cz.michalbelohoubek.interviewshowcase.app.feature.competition.R
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventProperty
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus
import cz.michalbelohoubek.interviewshowcase.core.model.AuthStatus.Companion.isAnonymouslyLoggedIn
import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import kotlinx.coroutines.launch

sealed interface DrawerMenuItem {
    data object PrivacyPolicy : DrawerMenuItem
    data object TermsAndConditions : DrawerMenuItem
}

@Composable
fun CompetitionsRoute(
    loggedOut: () -> Unit,
    onCompetitionClicked: (Competition) -> Unit,
    onAddCompetitionClicked: () -> Unit,
    onUiModeChange: (isDarkMode: Boolean) -> Unit,
    onDrawerMenuItemClicked: (DrawerMenuItem) -> Unit,
    viewModel: CompetitionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState.authStatus) {
        AuthStatus.LOADING -> {
            STProgressBar()
        }
        AuthStatus.LOGGED_OUT -> {
            LaunchedEffect(uiState.authStatus) {
                loggedOut()
            }
        }
        AuthStatus.LOGGED_ANONYMOUSLY, AuthStatus.LOGGED_IN -> {
            LaunchedEffect(viewModel, uiState.authStatus) {
                viewModel.forceUpdateCompetitions()
            }
            val context = LocalContext.current
            CompetitionsScreen(
                uiState = uiState,
                onAddCompetitionClicked = onAddCompetitionClicked,
                onUiModeChange = onUiModeChange,
                onLogoutClick = {
                    viewModel.invalidate()
                    viewModel.logout()
                },
                onDeleteCompetitionClick = {
                    viewModel.displayDialog(CompetitionsDialog.DeleteCompetition(it))
                },
                onDeleteAccountClick = {
                    viewModel.displayDialog(CompetitionsDialog.DeleteAccount)
                },
                onCompetitionClick = onCompetitionClicked,
                onClearMessages = viewModel::clearMessages,
                onSortByChanged = viewModel::changeSortedBy,
                onDrawerMenuItemClicked = onDrawerMenuItemClicked,
                onSignIn = {
                    viewModel.signInWithCredentialManager(context)
                }
            )
        }
    }
    val dialogToDisplay by viewModel.dialogToDisplay.collectAsStateWithLifecycle()
    when (val _dialog = dialogToDisplay) {
        is CompetitionsDialog.AccountCollision -> {
            AccountCollisionDialog(
                onDismissDialog = {
                    AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.LOGIN_FROM_DRAWER_CANCELLED))
                    viewModel.dismissDialog()
                },
                onSwitchAccounts = {
                    viewModel.dismissDialog()
                    viewModel.signInWithGoogle(_dialog.credential)
                }
            )
        }
        CompetitionsDialog.DeleteAccount -> {
            DeleteAccountDialog(
                onDismissDialog = {
                    viewModel.dismissDialog()
                },
                onDeleteConfirmed = {
                    viewModel.dismissDialog()
                    viewModel.invalidate()
                    viewModel.deleteAccount()
                }
            )
        }
        is CompetitionsDialog.DeleteCompetition -> {
            DeleteCompetitionDialog(
                onDismissDialog = {
                    viewModel.dismissDialog()
                },
                onDeleteConfirmed = {
                    viewModel.deleteCompetition(it)
                    viewModel.dismissDialog()
                },
                competition = _dialog.competition
            )
        }
        CompetitionsDialog.None -> {}
        CompetitionsDialog.Review -> {}
        CompetitionsDialog.Upgrade -> {}
    }
}

@Composable
fun CompetitionsScreen(
    modifier: Modifier = Modifier,
    uiState: CompetitionsScreenUiState,
    onUiModeChange: (isDarkMode: Boolean) -> Unit,
    onAddCompetitionClicked: () -> Unit,
    onLogoutClick: () -> Unit,
    onCompetitionClick: (Competition) -> Unit,
    onDeleteCompetitionClick: (Competition) -> Unit,
    onDeleteAccountClick: () -> Unit,
    onClearMessages: () -> Unit,
    onSortByChanged: (SortBy) -> Unit,
    onSignIn: () -> Unit,
    onDrawerMenuItemClicked: (DrawerMenuItem) -> Unit
) {
    trackScreenView(AnalyticsUtil.Screens.COMPETITIONS)

    val isAnonymousLogin = uiState.authStatus.isAnonymouslyLoggedIn

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    STScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            STDrawerToolbar(title = R.string.competitions_title) {
                coroutineScope.launch { scaffoldState.drawerState.open() }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.START_COMPETITION_CREATION))
                onAddCompetitionClicked()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        drawerContent = {
            DrawerContent(
                isAnonymousLogin = isAnonymousLogin,
                email = uiState.user?.email,
                displayName = uiState.user?.name,
                onUiModeChange = onUiModeChange,
                onLogoutClick = onLogoutClick,
                onDeleteAccountClick = onDeleteAccountClick,
                onSignInClick = {
                    AnalyticsUtil.logEvent(EventProperty(EventPropertyEnum.START_LOGIN_FROM_DRAWER))
                    onSignIn()
                },
                onPrivacyClick = {
                    onDrawerMenuItemClicked(DrawerMenuItem.PrivacyPolicy)
                },
                onTermsAndConditionsClick = {
                    onDrawerMenuItemClicked(DrawerMenuItem.TermsAndConditions)
                }
            )
        },
        drawerElevation = 0.dp,
        drawerGesturesEnabled = true,
        snackbarHost = { SnackbarHost(it) },
    ) { paddingValues ->

        DisplayError(
            snackbarHostState = scaffoldState.snackbarHostState,
            error = uiState.errorMessage
        ) {
            onClearMessages()
        }

        DisplayInfo(
            snackbarHostState = scaffoldState.snackbarHostState,
            info = uiState.infoMessage
        ) {
            onClearMessages()
        }

        if (uiState.isLoading) {
            STProgressBar()
        }
        when (val _uiState = uiState.competitionsUiState) {
            is CompetitionsUiState.Error -> {
                ErrorScreen(
                    paddingValues = paddingValues,
                    error = _uiState.error
                )
            }
            CompetitionsUiState.Loading -> {
                STProgressBar()
            }
            is CompetitionsUiState.Success -> {
                val competitions = _uiState.competitions
                val sortBy = _uiState.sortBy
                Column {
                    if (competitions.isEmpty()) {
                        EmptyScreen(
                            text = stringResource(R.string.competitions_empty)
                        )
                    } else {
                        SortByHeader(
                            modifier = Modifier.fillMaxWidth(),
                            sortBy = sortBy,
                            onSortByChanged = onSortByChanged
                        )
                        CompetitionsList(
                            competitions = competitions,
                            onCompetitionClicked = onCompetitionClick,
                            onDeleteClick = onDeleteCompetitionClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SortByHeader(
    modifier: Modifier = Modifier,
    sortBy: SortBy,
    onSortByChanged: (SortBy) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.sort_competitions_title))
        STHorizontalChips(
            title = null,
            options = SortBy.values(),
            optionToReadable = {
                stringResource(id = it.stringRes)
            },
            selectedOption = sortBy,
            enabled = true,
            onSelectedOption = {
                onSortByChanged(it)
            }
        )
    }
}

@Composable
fun CompetitionsList(
    modifier: Modifier = Modifier,
    competitions: List<Competition>,
    onCompetitionClicked: (Competition) -> Unit,
    onDeleteClick: (Competition) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        competitions.distinctBy { it.id }.forEach {
            item(key = it.id) {
                CompetitionItem(
                    modifier = Modifier.animateItem(),
                    competition = it,
                    onClick = onCompetitionClicked,
                    onDeleteClick = {
                        onDeleteClick(it)
                    }
                )
            }
        }
    }
}

@Composable
fun CompetitionItem(
    modifier: Modifier = Modifier,
    competition: Competition,
    onClick: (Competition) -> Unit,
    onDeleteClick: (Competition) -> Unit
) {
    CardItem(
        modifier = modifier.padding(horizontal = 16.dp),
        title = competition.name,
        iconRes = competition.sport.iconResource,
        subtitle = stringResource(id = competition.type.toReadable()),
        onClick = {
            onClick(competition)
        },
        action = {
            onDeleteClick(competition)
        },
        actionImage = Icons.Default.Delete
    )
}

@Composable
fun DeleteAccountDialog(
    onDismissDialog: () -> Unit,
    onDeleteConfirmed: () -> Unit,
) {
    STAlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismiss = onDismissDialog,
        confirmText = stringResource(cz.michalbelohoubek.interviewshowcase.app.core.ui.R.string.general_delete),
        onConfirm = onDeleteConfirmed,
        dismissText = stringResource(cz.michalbelohoubek.interviewshowcase.app.core.ui.R.string.general_cancel),
        title = stringResource(R.string.delete_account_confirmation_title),
        message = stringResource(R.string.delete_account_confirmation_message),
        style = DialogStyle.WARNING
    )
}

@Composable
fun DeleteCompetitionDialog(
    competition: Competition,
    onDismissDialog: () -> Unit,
    onDeleteConfirmed: (Competition) -> Unit,
) {
    STAlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        onDismiss = onDismissDialog,
        confirmText = stringResource(R.string.account_collision_dialog_switch_accounts),
        onConfirm = { onDeleteConfirmed(competition) },
        dismissText = stringResource(cz.michalbelohoubek.interviewshowcase.app.core.ui.R.string.general_cancel),
        title = stringResource(R.string.delete_competition_confirmation_title),
    )
}

@Composable
fun AccountCollisionDialog(
    onDismissDialog: () -> Unit,
    onSwitchAccounts: () -> Unit,
) {
    STAlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismiss = onDismissDialog,
        confirmText = stringResource(R.string.account_collision_dialog_switch_accounts),
        onConfirm = onSwitchAccounts,
        dismissText = stringResource(cz.michalbelohoubek.interviewshowcase.app.core.ui.R.string.general_cancel),
        title = stringResource(R.string.account_collision_dialog_title),
        message = stringResource(R.string.account_collision_dialog_message),
        style = DialogStyle.WARNING
    )
}