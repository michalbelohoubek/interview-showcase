package cz.michalbelohoubek.interviewshowcase.data.di

import android.app.Application
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cz.michalbelohoubek.interviewshowcase.common.R
import cz.michalbelohoubek.interviewshowcase.data.repository.AuthRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.AuthRepositoryImpl
import cz.michalbelohoubek.interviewshowcase.data.repository.CompetitionRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.CompetitionRepositoryImpl
import cz.michalbelohoubek.interviewshowcase.data.repository.UserSettingsRepository
import cz.michalbelohoubek.interviewshowcase.data.repository.UserSettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun providesCompetitionRepository(
        competitionRepository: CompetitionRepositoryImpl
    ): CompetitionRepository

    @Binds
    fun bindsAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    fun providesUserSettingsRepository(
        userSettingsRepository: UserSettingsRepositoryImpl
    ): UserSettingsRepository

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        fun provideContext(
            app: Application
        ): Context = app.applicationContext

        @Provides
        fun provideSignInWithGoogleId(
            app: Application
        ) = GetSignInWithGoogleOption.Builder(app.getString(R.string.web_client_id))
            .build()

        @Provides
        fun provideGetCredentialsRequest(
            getSignInWithGoogleOption: GetSignInWithGoogleOption,
        ) = GetCredentialRequest.Builder()
            .addCredentialOption(getSignInWithGoogleOption)
            .build()

        @Provides
        fun provideCredentialManager(
            app: Application
        ) = CredentialManager.create(app)
    }
}
