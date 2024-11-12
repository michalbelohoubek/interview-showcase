plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.library.compose)
    alias(libs.plugins.interviewshowcase.android.feature)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.interviewshowcase.android.hilt)
}

dependencies {
    implementation(project(":feature:shared-auth"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.identity.googleid)
}
android {
    namespace = "cz.michalbelohoubek.interviewshowcase.app.feature.competition"
}
