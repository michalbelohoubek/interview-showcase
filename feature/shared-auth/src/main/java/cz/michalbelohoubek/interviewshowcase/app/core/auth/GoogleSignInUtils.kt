package cz.michalbelohoubek.interviewshowcase.app.core.auth

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil

object GoogleSignInUtils {
    fun getGoogleIdToken(credential: Credential): GoogleIdTokenResult {
        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data).idToken
                        GoogleAuthProvider.getCredential(googleIdTokenCredential, null).run {
                            GoogleIdTokenResult.Success(this)
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        AnalyticsUtil.logLoginError("google_id_token_error", e.message.orEmpty(), e)
                        GoogleIdTokenResult.Error.GoogleIdTokenParsingError
                    } catch (e: Exception) {
                        AnalyticsUtil.logLoginError("google_id_token_error_general", e.message.orEmpty(), e)
                        GoogleIdTokenResult.Error.General(e.message.toString())
                    }
                } else {
                    AnalyticsUtil.logLoginError("unrecognized_credentials_no_google", credential.type, Exception("unrecognized_credentials_no_google"))
                    GoogleIdTokenResult.Error.UnrecognizedCredentials
                }
            }
            else -> {
                AnalyticsUtil.logLoginError("unrecognized_credentials", credential.type, Exception("unrecognized_credentials"))
                GoogleIdTokenResult.Error.UnrecognizedCredentials
            }
        }
    }
}

sealed interface GoogleIdTokenResult {
    data class Success(val authCredentials: AuthCredential) : GoogleIdTokenResult
    sealed interface Error : GoogleIdTokenResult {
        data class General(val message: String) : Error
        data object GoogleIdTokenParsingError : Error
        data object UnrecognizedCredentials : Error
    }
}
