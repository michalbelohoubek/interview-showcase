plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.feature)
    alias(libs.plugins.interviewshowcase.android.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.google.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.identity.googleid)
}

android {
    namespace = "cz.michalbelohoubek.interviewshowcase.app.core.auth"
}