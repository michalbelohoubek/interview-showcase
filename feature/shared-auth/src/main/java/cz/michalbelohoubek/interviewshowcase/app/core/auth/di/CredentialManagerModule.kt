package cz.michalbelohoubek.interviewshowcase.app.core.auth.di

import android.content.Context
import androidx.credentials.GetCredentialRequest
import cz.michalbelohoubek.interviewshowcase.app.core.auth.CredentialManagerUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CredentialManagerModule {

    @Singleton
    @Provides
    fun providesCredentialManagerUtils(
        @ApplicationContext context: Context,
        getCredentialRequest: GetCredentialRequest
    ): CredentialManagerUtils {
        return CredentialManagerUtils(context, getCredentialRequest)
    }
}