package cz.michalbelohoubek.interviewshowcase.network.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import cz.michalbelohoubek.interviewshowcase.network.datasource.CompetitionDataSource
import cz.michalbelohoubek.interviewshowcase.network.datasource.firestore.FirestoreCompetitionDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsCompetitionDataSource(
        dataSource: FirestoreCompetitionDataSource // TODO: can't be internal now, so I am exposing Firebase to the core:data module
    ): CompetitionDataSource

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseFirestore(): FirebaseFirestore {
            val settings = firestoreSettings {
                isPersistenceEnabled = true
                cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            }
            return Firebase.firestore.apply {
                firestoreSettings = settings
            }
        }

        @Provides
        @Singleton
        fun providesFirebaseCrashlytics(): FirebaseCrashlytics {
            return FirebaseCrashlytics.getInstance()
        }
    }
}