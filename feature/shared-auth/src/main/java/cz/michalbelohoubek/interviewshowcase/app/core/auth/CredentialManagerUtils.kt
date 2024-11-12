package cz.michalbelohoubek.interviewshowcase.app.core.auth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import javax.inject.Inject

class CredentialManagerUtils @Inject constructor(
    private val context: Context,
    private val getCredentialRequest: GetCredentialRequest
) {

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    suspend fun getCredential(activityContext: Context): CredentialsResponse {
        return try {
            val result = credentialManager.getCredential(
                request = getCredentialRequest,
                context = activityContext,
            )
            CredentialsResponse.Success(result.credential)
        } catch (e: GetCredentialCancellationException) {
            AnalyticsUtil.logLoginError("cancelled_by_user", e.errorMessage.toString(), e)
            CredentialsResponse.Error.UserCancelled
        } catch (e: NoCredentialException) {
            AnalyticsUtil.logLoginError("no_credentials", e.errorMessage.toString(), e)
            CredentialsResponse.Error.NoCredentials
        } catch (e: GetCredentialException) {
            AnalyticsUtil.logLoginError("get_credential_error", e.type + e.errorMessage.toString(), e)
            CredentialsResponse.Error.GetCredentialsError(e.message.toString())
        } catch (e: Exception) {
            AnalyticsUtil.logLoginError("general", e.message.orEmpty(), e)
            CredentialsResponse.Error.General(e.message.toString())
        }
    }
}

sealed interface CredentialsResponse {
    data class Success(val credential: Credential) : CredentialsResponse
    sealed interface Error : CredentialsResponse {
        data object NoCredentials : Error
        data object UserCancelled : Error
        data class GetCredentialsError(val message: String) : Error
        data class General(val message: String) : Error
    }
}