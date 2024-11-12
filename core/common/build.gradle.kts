plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.hilt)
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.mixpanel.android)
}
android {
    namespace = "cz.michalbelohoubek.interviewshowcase.common"
}
