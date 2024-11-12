package cz.michalbelohoubek.interviewshowcase.app.core.auth.utils

import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialsResponse
import cz.michalbelohoubek.interviewshowcase.app.core.auth.GoogleIdTokenResult
import cz.michalbelohoubek.interviewshowcase.app.core.auth.R
import cz.michalbelohoubek.interviewshowcase.app.core.ui.states.UiError
import cz.michalbelohoubek.interviewshowcase.data.repository.SignInResponse

fun SignInResponse.Error.asUiError(): UiError =
    when (this) {
        is SignInResponse.Error.AccountCollision -> UiError.Res(R.string.sign_in_error_account_collision)
        is SignInResponse.Error.InvalidCredentials -> UiError.Res(R.string.sign_in_error_invalid_credentials)
        is SignInResponse.Error.OtherError -> UiError.Plain(this.error.message.orEmpty())
    }

fun GoogleIdTokenResult.Error.asUiError(): UiError =
    when (this) {
        is GoogleIdTokenResult.Error.General -> UiError.Plain(this.message)
        GoogleIdTokenResult.Error.GoogleIdTokenParsingError -> UiError.Res(R.string.sign_in_error_id_token_parsing)
        GoogleIdTokenResult.Error.UnrecognizedCredentials -> UiError.Res(R.string.sign_in_error_no_credentials)
    }

fun CredentialsResponse.Error.asUiError(): UiError =
    when (this) {
        is CredentialsResponse.Error.General -> UiError.Plain(this.message)
        is CredentialsResponse.Error.GetCredentialsError -> UiError.Plain(this.message)
        CredentialsResponse.Error.NoCredentials -> UiError.Res(R.string.sign_in_error_no_credentials)
        CredentialsResponse.Error.UserCancelled -> UiError.Res(R.string.sign_in_error_cancelled)
    }