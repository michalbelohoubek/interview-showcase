package cz.michalbelohoubek.interviewshowcase

import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import cz.michalbelohoubek.interviewshowcase.common.analytics.AppContextWrapper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InterviewShowcaseApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(AppContextWrapper(base))
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance()
    }
}